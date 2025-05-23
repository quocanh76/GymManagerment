package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotActivity extends AppCompatActivity {
    private EditText nameEditText, mobileEditText, emailEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_forgot);
        nameEditText = findViewById(R.id.editTextName);
        mobileEditText = findViewById(R.id.editTextMobile);
        emailEditText = findViewById(R.id.editTextEmail);


        Button sendcodebtn = findViewById(R.id.sendCode);
        sendcodebtn.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleForgotPassword() {
            String name = nameEditText.getText().toString().trim();
            String mobile = mobileEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Name required");
                return;
            }
            if (TextUtils.isEmpty(mobile)) {
                mobileEditText.setError("Mobile number required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError("Email required");
                return;
            }
        Toast.makeText(this, "Password reset code has been sent to your email!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
