package com.example.raksha;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.raksha.db.RakshaDBHelper;
import com.example.raksha.utils.SharedPrefManager;

public class EditProfileActivity extends AppCompatActivity {

    private TextView phoneDisplay;
    private EditText nameInput, ageInput, occupationInput;
    private Button saveBtn;
    private RakshaDBHelper dbHelper;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new SharedPrefManager(this);
        if (prefManager.isDarkMode()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_edit_profile);

        dbHelper = new RakshaDBHelper(this);

        phoneDisplay = findViewById(R.id.phone_display);
        nameInput = findViewById(R.id.name_input);
        ageInput = findViewById(R.id.age_input);
        occupationInput = findViewById(R.id.occupation_input);
        saveBtn = findViewById(R.id.save_btn);

        loadUserData();
        saveBtn.setOnClickListener(v -> saveProfile());
    }

    private void loadUserData() {
        int userId = prefManager.getUserId();
        Cursor cursor = dbHelper.getUserData(userId);

        if (cursor != null && cursor.moveToFirst()) {
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            phoneDisplay.setText("Phone: " + phone);
            nameInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            ageInput.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("age"))));
            occupationInput.setText(cursor.getString(cursor.getColumnIndexOrThrow("occupation")));
            cursor.close();
        }
    }

    private void saveProfile() {
        String name = nameInput.getText().toString().trim();
        String ageStr = ageInput.getText().toString().trim();
        String occupation = occupationInput.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        int userId = prefManager.getUserId();

        if (dbHelper.updateUser(userId, name, age, occupation)) {
            prefManager.setUserName(name);
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}