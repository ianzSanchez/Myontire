package com.example.myontire;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginPage extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.LoginButton);

        loginButton.setOnClickListener(v -> validateInputs());
    }

    private void validateInputs() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and Password must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email's format is invalid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            Toast.makeText(this, "Passwords must be at least 8 characters long and contain both letters and numbers!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Successful login!", Toast.LENGTH_SHORT).show();

        // Intent ke Homepage setelah login sukses
        Intent intent = new Intent(LoginPage.this, homepage.class);
        startActivity(intent);
        finish(); // Tutup halaman login agar tidak bisa kembali ke login dengan tombol back
    }
}
