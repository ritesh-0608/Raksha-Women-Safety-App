package com.example.raksha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.raksha.utils.SharedPrefManager;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout editProfileItem, languageItem;
    private Switch darkModeSwitch;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new SharedPrefManager(this);

        // Apply theme BEFORE setContentView
        if (prefManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppThemeDark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_settings);

        editProfileItem = findViewById(R.id.edit_profile_item);
        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        languageItem = findViewById(R.id.language_item);

        editProfileItem.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, EditProfileActivity.class)));
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefManager.setDarkMode(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // Restart this activity to apply theme
            recreate();
        });
        languageItem.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this, LanguageActivity.class)));

        darkModeSwitch.setChecked(prefManager.isDarkMode());
    }
}