package com.example.gymmanagerment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddMemberActivity extends AppCompatActivity {
    private EditText edtName, edtPhone, edtStart, edtPackage;
    private FirebaseFirestore db;
    private Map<String, Object> pendingMember;
    private ProgressDialog progressDialog;

    private static final int PAYMENT_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        initializeViews();
        setupFirestore();
        setupDatePicker();
        setupSaveButton();
    }

    private void initializeViews() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPackage = findViewById(R.id.edtpackage);
        edtStart = findViewById(R.id.edtstart);
        Button btnSave = findViewById(R.id.btnSave);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("\u0110ang x\u1eed l\u00fd...");
        progressDialog.setCancelable(false);
    }

    private void setupFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    private void setupDatePicker() {
        edtStart.setOnClickListener(v -> showDatePicker());
    }

    private void setupSaveButton() {
        findViewById(R.id.btnSave).setOnClickListener(v -> validateAndPrepareMember());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, day) -> edtStart.setText(formatDate(day, month + 1, year)),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private String formatDate(int day, int month, int year) {
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
    }

    private void validateAndPrepareMember() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String packageId = edtPackage.getText().toString().trim();
        String startDate = edtStart.getText().toString().trim();

        if (validateInputs(name, phone, packageId, startDate)) {
            fetchPackageDetails(packageId, name, phone, startDate);
        }
    }

    private boolean validateInputs(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void fetchPackageDetails(String packageId, String name, String phone, String startDate) {
        progressDialog.show();

        db.collection("Packages").document(packageId)
                .get()
                .addOnSuccessListener(document -> {
                    progressDialog.dismiss();

                    if (document.exists()) {
                        try {
                            Long priceObj = document.getLong("price");
                            Long durationObj = document.getLong("duration");

                            if (priceObj == null || durationObj == null) {
                                throw new Exception("Thiếu thông tin gói tập");
                            }

                            long price = priceObj;
                            long duration = durationObj;

                            preparePendingMember(name, phone, startDate, document.getId(), duration);
                            launchPaymentActivity(price);

                        } catch (Exception e) {
                            Toast.makeText(this, "Lỗi dữ liệu gói tập: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy gói tập", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi lấy thông tin gói tập: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void preparePendingMember(String name, String phone, String startDate, String packageId, long duration) {
        pendingMember = new HashMap<>();
        pendingMember.put("name", name);
        pendingMember.put("phone", phone);
        pendingMember.put("package_id", packageId);
        pendingMember.put("start_date", startDate);
        pendingMember.put("end_date", calculateEndDate(startDate, (int) duration));
    }

    private String calculateEndDate(String startDate, int months) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = sdf.parse(startDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, months);

            return sdf.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void launchPaymentActivity(long price) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("PRICE", price);
        startActivityForResult(intent, PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYMENT_REQUEST_CODE) {
            handlePaymentResult(resultCode);
        }
    }

    private void handlePaymentResult(int resultCode) {
        if (resultCode == RESULT_OK) {
            saveMemberToFirestore();
        } else {
            Toast.makeText(this, "Lỗi thanh toán", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveMemberToFirestore() {
        progressDialog.show();

        db.collection("Members")
                .add(pendingMember)
                .addOnSuccessListener(ref -> {
                    progressDialog.dismiss();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("MEMBER_ID", ref.getId());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Lỗi lưu thông tin hội viên: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToMemberDetail(String memberId) {
        Intent intent = new Intent(this, MemberDetailActivity.class);
        intent.putExtra("MEMBER_ID", memberId);
        startActivity(intent);
        finish();
    }
}
