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

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, phoneInput, ageInput, occupationInput, passwordInput, confirmPasswordInput;
    private Button signupBtn;
    private TextView loginText;
    private RakshaDBHelper dbHelper;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new RakshaDBHelper(this);
        prefManager = new SharedPrefManager(this);

        nameInput = findViewById(R.id.name_input);
        phoneInput = findViewById(R.id.phone_input);
        ageInput = findViewById(R.id.age_input);
        occupationInput = findViewById(R.id.occupation_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        signupBtn = findViewById(R.id.signup_btn);
        loginText = findViewById(R.id.login_text);

        signupBtn.setOnClickListener(v -> handleSignup());
        loginText.setOnClickListener(v -> finish());
    }

    private void handleSignup() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        String occupation = occupationInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || ageStr.isEmpty() || occupation.isEmpty() || password.isEmpty()) {
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

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkPhoneExists(phone)) {
            Toast.makeText(this, "Phone number already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);

        if (dbHelper.addUser(name, phone, age, occupation, password)) {
            int userId = dbHelper.getUserId(phone);
            prefManager.setUserLoggedIn(true);
            prefManager.setUserId(userId);
            prefManager.setUserPhone(phone);
            prefManager.setUserName(name);

            Toast.makeText(this, getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignupActivity.this, SosContactSetupActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
        }
    }
}