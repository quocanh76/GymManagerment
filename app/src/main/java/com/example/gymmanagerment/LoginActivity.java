package com.example.gymmanagerment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanagerment.ForgotActivity;
import com.example.gymmanagerment.MainActivity;
import com.example.gymmanagerment.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_login);

        editTextEmail = findViewById(R.id.edtEmail);
        editTextPassword = findViewById(R.id.edtPassword);
        Button loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(view -> handleLogin());
    }

    private void handleLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Basic validation
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            return;
        }

        // Simulated login check
        if (email.equals("admin") && password.equals("123")) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            // Proceed to next screen
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewRegisterClicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void viewForgotPassword(View view) {
        Intent intent = new Intent(this, ForgotActivity.class);
        startActivity(intent);

    }

}
