package com.example.gymmanagerment;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    CardView cardMember, cardPackage, cardCheck, cardReport, cardExpired,cardRenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardMember = findViewById(R.id.cardMember);
        cardPackage = findViewById(R.id.cardPackage);
        cardCheck = findViewById(R.id.cardCheck);
        cardReport = findViewById(R.id.cardReport);
        cardExpired = findViewById(R.id.cardExpired);
        cardRenew = findViewById(R.id.cardRenew);

        cardMember.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MemberActivity.class));
        });

        cardPackage.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PackagesActivity.class));
        });

        cardCheck.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
        });
        cardReport.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
        });
        cardExpired.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ExpiredActivity.class));
        });
        cardRenew.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RenewActivity.class));
        });
    }
}
