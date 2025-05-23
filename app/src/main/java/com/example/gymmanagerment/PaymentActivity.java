package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        long price = getIntent().getLongExtra("PRICE", -1);
        if (price == -1) {
            price = getIntent().getLongExtra("AMOUNT", 0);
        }


        TextView tvAmount = findViewById(R.id.txtAmount);

        tvAmount.setText(String.format("Thành tiền: %,dđ", price).replace(",", "."));

        findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        findViewById(R.id.btnCancel).setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}
