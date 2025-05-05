package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    CardView cardMember, cardPackage, cardCheck, cardReport, cardexpired;
    FloatingActionButton fabThemThanhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // file XML bạn đã tạo

        // Ánh xạ các thành phần
        cardMember = findViewById(R.id.cardMember);
        cardPackage = findViewById(R.id.cardPackage);
        cardCheck = findViewById(R.id.cardCheck);
        cardReport = findViewById(R.id.cardReport);
        cardexpired = findViewById(R.id.cardExpired);

        // Xử lý click cho từng thẻ
        cardMember.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MemberActivity.class));
        });

        cardPackage.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PackagesActivity.class));
        });
//
//        cardCheck.setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, CheckActivity.class));
//        });
//
//        cardReport.setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, ReportActivity.class));
//        });
//
//        cardExpired.setOnClickListener(v -> {
//            startActivity(new Intent(MainActivity.this, ExpiredActivity.class));
//        });
    }
}
