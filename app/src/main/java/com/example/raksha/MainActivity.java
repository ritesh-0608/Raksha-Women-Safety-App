package com.example.raksha;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.raksha.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new SharedPrefManager(this);

        // Apply theme BEFORE setContentView - NO RECREATION
        if (prefManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppThemeDark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_main);

        // Show splash for 2 seconds then route to appropriate activity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (prefManager.isUserLoggedIn()) {
                if (prefManager.isSosSetup()) {
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, SosContactSetupActivity.class));
                }
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}
