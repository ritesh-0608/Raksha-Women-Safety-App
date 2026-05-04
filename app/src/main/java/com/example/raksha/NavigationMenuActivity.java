package com.example.raksha;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.raksha.utils.SharedPrefManager;

public class NavigationMenuActivity extends AppCompatActivity {

    private LinearLayout settingsItem, sosContactItem, policeItem, hospitalItem, medicalItem, aboutItem, logoutItem;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);

        prefManager = new SharedPrefManager(this);

        settingsItem = findViewById(R.id.settings_item);
        sosContactItem = findViewById(R.id.sos_contact_item);
        policeItem = findViewById(R.id.police_item);
        hospitalItem = findViewById(R.id.hospital_item);
        medicalItem = findViewById(R.id.medical_item);
        aboutItem = findViewById(R.id.about_item);
        logoutItem = findViewById(R.id.logout_item);

        settingsItem.setOnClickListener(v -> startActivity(new Intent(NavigationMenuActivity.this, SettingsActivity.class)));
        sosContactItem.setOnClickListener(v -> startActivity(new Intent(NavigationMenuActivity.this, EditSosContactActivity.class)));
        policeItem.setOnClickListener(v -> startActivity(new Intent(NavigationMenuActivity.this, PoliceStationActivity.class)));
        hospitalItem.setOnClickListener(v -> startActivity(new Intent(NavigationMenuActivity.this, HospitalActivity.class)));
        medicalItem.setOnClickListener(v -> startActivity(new Intent(NavigationMenuActivity.this, MedicalShopActivity.class)));
        aboutItem.setOnClickListener(v -> startActivity(new Intent(NavigationMenuActivity.this, AboutUsActivity.class)));
        logoutItem.setOnClickListener(v -> handleLogout());
    }

    private void handleLogout() {
        prefManager.logout();
        startActivity(new Intent(NavigationMenuActivity.this, LoginActivity.class));
        finishAffinity();
    }
}