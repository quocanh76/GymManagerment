package com.example.gymmanagerment;

import static java.lang.Integer.parseInt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

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
    EditText edtName, edtPhone, edtstart, edtpackage;
    Button btnSave;
    FirebaseFirestore db;
    String endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtpackage = findViewById(R.id.edtpackage);
        edtstart = findViewById(R.id.edtstart);
        btnSave = findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();
        btnSave.setOnClickListener(v -> saveMember());

        edtstart.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddMemberActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        // Định dạng ngày và hiển thị
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                        edtstart.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });


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


    private void saveMember() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String packagename = edtpackage.getText().toString().trim();
        String startdate = edtstart.getText().toString().trim();

        // Kiểm tra đầu vào
        if (name.isEmpty() || phone.isEmpty() || packagename.isEmpty() || startdate.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int packageValue;
        try {
            packageValue = Integer.parseInt(packagename);  // dùng giá trị số
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Gói tháng phải là số", Toast.LENGTH_SHORT).show();
            return;
        }

        String enddate = calculateEndDate(startdate, packageValue);

        Map<String, Object> member = new HashMap<>();
        member.put("name", name);
        member.put("phone", phone);
        member.put("package_id", packagename);
        member.put("start_date", startdate);
        member.put("end_date", enddate);

        db.collection("Members")
                .add(member)
                .addOnSuccessListener(documentReference -> {
                    String newMemberId = documentReference.getId();
                    Intent intent = new Intent(AddMemberActivity.this, MemberDetailActivity.class);
                    intent.putExtra("memberId", newMemberId);
                    startActivity(intent);
                    finish();
                });
    }
}

