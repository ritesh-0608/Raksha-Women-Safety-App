package com.example.raksha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.raksha.db.RakshaDBHelper;
import com.example.raksha.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneInput, passwordInput;
    private Button loginBtn;
    private TextView signupText;
    private RakshaDBHelper dbHelper;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new RakshaDBHelper(this);
        prefManager = new SharedPrefManager(this);

        phoneInput = findViewById(R.id.phone_input);
        passwordInput = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        signupText = findViewById(R.id.signup_text);

        loginBtn.setOnClickListener(v -> handleLogin());
        signupText.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void handleLogin() {
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validation
        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.length() != 10) {
            Toast.makeText(this, getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.matches("\\d+")) {
            Toast.makeText(this, "Phone number must contain only digits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check credentials in database
        if (dbHelper.checkUser(phone, password)) {
            int userId = dbHelper.getUserId(phone);

            // Save to SharedPreferences
            prefManager.setUserLoggedIn(true);
            prefManager.setUserId(userId);
            prefManager.setUserPhone(phone);

            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();

            // Navigate to SOS Setup
            startActivity(new Intent(LoginActivity.this, SosContactSetupActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid phone or password", Toast.LENGTH_SHORT).show();
            passwordInput.setText("");
        }
    }
}