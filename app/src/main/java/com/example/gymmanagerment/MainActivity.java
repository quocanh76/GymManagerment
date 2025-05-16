package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    CardView cardMember, cardPackage, cardCheck, cardReport, cardExpired;
    FloatingActionButton fabThemThanhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardMember = findViewById(R.id.cardMember);
        cardPackage = findViewById(R.id.cardPackage);
        cardCheck = findViewById(R.id.cardCheck);
        cardReport = findViewById(R.id.cardReport);
        cardExpired = findViewById(R.id.cardExpired);

        cardMember.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MemberActivity.class));
        });

        cardPackage.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PackagesActivity.class));
        });

        cardCheck.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
        });
//
        cardReport.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
        });
//
        cardExpired.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExpiredActivity.class));
        });
    }
}
