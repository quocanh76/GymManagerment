package com.example.gymmanagerment;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditMemberActivity extends AppCompatActivity {
    EditText edtName, edtPhone, edtPackage, edtStart;
    Button btnSave;
    FirebaseFirestore db;
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        edtName = findViewById(R.id.edtName);
        btnSave = findViewById(R.id.btnSave);
        edtPhone = findViewById(R.id.edtPhone);
        edtPackage = findViewById(R.id.edtPackage);
        edtStart = findViewById(R.id.edtStart);

        db = FirebaseFirestore.getInstance();

        memberId = getIntent().getStringExtra("memberId");
        loadMemberData();

        btnSave.setOnClickListener(v -> updateMemberData());
    }

    private void loadMemberData() {
        db.collection("Members").document(memberId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                String phone = documentSnapshot.getString("phone");
                String packageName = documentSnapshot.getString("package_id");
                String startDate = documentSnapshot.getString("start_date");

                edtName.setText(name);
                edtPhone.setText(phone);
                edtPackage.setText(packageName);
                edtStart.setText(startDate);
            }
        });
    }

    private void updateMemberData() {
        String newName = edtName.getText().toString();
        String newPhone = edtPhone.getText().toString();
        String newPackage = edtPackage.getText().toString();
        String newStartDate = edtStart.getText().toString();

        int packageValue;
        try {
            packageValue = Integer.parseInt(newPackage);  // dùng giá trị số
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Gói tháng phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        String enddate = calculateEndDate(newStartDate, packageValue);
        if (!newName.isEmpty() && !newPhone.isEmpty() && !newPackage.isEmpty() && !newStartDate.isEmpty()) {
            db.collection("Members").document(memberId)
                    .update("name", newName, "phone", newPhone, "package_id", newPackage, "start_date", newStartDate, "end_date", enddate)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditMemberActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish(); // Quay lại màn hình trước
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(EditMemberActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }
    private String calculateEndDate(String startDateStr, int packageDays) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date startDate = sdf.parse(startDateStr);
            if (startDate != null) {
                calendar.setTime(startDate);
                calendar.add(Calendar.DAY_OF_MONTH, 30 * packageDays); // Cộng ngày
                return sdf.format(calendar.getTime());   // Trả về chuỗi ngày
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}

