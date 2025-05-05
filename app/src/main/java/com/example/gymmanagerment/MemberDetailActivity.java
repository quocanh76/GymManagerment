package com.example.gymmanagerment;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberDetailActivity extends AppCompatActivity {
    TextView txtName, txtPhone, txtPackage, txtStartDate, txtEndDate;
    FirebaseFirestore db;
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        // Ánh xạ view
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtPackage = findViewById(R.id.txtPackageId);
        txtStartDate = findViewById(R.id.txtStartDate);
        txtEndDate = findViewById(R.id.txtEndDate);

        // Nhận ID từ intent
        memberId = getIntent().getStringExtra("memberId");

        db = FirebaseFirestore.getInstance();

        if (memberId != null) {
            loadMemberDetail(memberId);
        }
    }

    private void loadMemberDetail(String memberId) {
        db.collection("Members").document(memberId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        String packageId = documentSnapshot.getString("package_id");
                        String startDate = documentSnapshot.getString("start_date");
                        String endDate = documentSnapshot.getString("end_date");

                        txtName.setText("Tên: " + name);
                        txtPhone.setText("SĐT: " + phone);
                        txtPackage.setText("Gói tập: " + packageId);
                        txtStartDate.setText("Ngày bắt đầu: " + startDate);
                        txtEndDate.setText("Ngày kết thúc: " + endDate);
                    }
                })
                .addOnFailureListener(e -> Log.e("MemberDetail", "Lỗi: " + e.getMessage()));
    }



}
