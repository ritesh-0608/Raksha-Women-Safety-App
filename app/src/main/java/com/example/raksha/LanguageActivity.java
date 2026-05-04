package com.example.raksha;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.raksha.utils.LocaleManager;
import com.example.raksha.utils.SharedPrefManager;

public class LanguageActivity extends AppCompatActivity {

    private RadioGroup languageGroup;
    private RadioButton englishRadio, hindiRadio;
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

        setContentView(R.layout.activity_language);

        languageGroup = findViewById(R.id.language_group);
        englishRadio = findViewById(R.id.english_radio);
        hindiRadio = findViewById(R.id.hindi_radio);

        String currentLanguage = prefManager.getLanguage();
        if (currentLanguage.equals("hi")) {
            hindiRadio.setChecked(true);
        } else {
            englishRadio.setChecked(true);
        }

        languageGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.english_radio) {
                setLanguage("en");
            } else if (checkedId == R.id.hindi_radio) {
                setLanguage("hi");
            }
        });
    }

    private void setLanguage(String language) {
        prefManager.setLanguage(language);
        LocaleManager.setLocale(this, language);
        recreate();
    }
}