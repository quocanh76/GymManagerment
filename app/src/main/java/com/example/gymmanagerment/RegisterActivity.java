package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, mobileEditText, emailEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);

        // Initialize views (make sure these IDs exist in your layout)
        nameEditText = findViewById(R.id.editTextName);
        mobileEditText = findViewById(R.id.editTextMobile);
        emailEditText = findViewById(R.id.editTextEmail);
        registerButton = findViewById(R.id.cirLoginButton);

        registerButton.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
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

        // Simulated success
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
        finish(); // go back to login
    }
    public void viewLoginClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
