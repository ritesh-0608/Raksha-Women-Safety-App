package com.example.raksha;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.raksha.utils.PermissionManager;
import com.example.raksha.utils.SharedPrefManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class DashboardActivity extends AppCompatActivity {

    private ImageButton sosBtn, menuBtn;
    private LinearLayout helplineCard, locationCard, messagesCard;
    private SharedPrefManager prefManager;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply theme BEFORE setContentView
        prefManager = new SharedPrefManager(this);
        if (prefManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppThemeDark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_dashboard);

        sosBtn = findViewById(R.id.sos_btn);
        menuBtn = findViewById(R.id.menu_btn);
        helplineCard = findViewById(R.id.helpline_card);
        locationCard = findViewById(R.id.location_card);
        messagesCard = findViewById(R.id.messages_card);

        // Initialize Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        sosBtn.setOnClickListener(v -> handleSOS());
        menuBtn.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, NavigationMenuActivity.class)));
        helplineCard.setOnClickListener(v -> dialHelpline());
        locationCard.setOnClickListener(v -> shareLocation());
        messagesCard.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, SafetyTipsActivity.class)));

        PermissionManager.requestAllPermissions(this);
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        try {
            // Request location updates using Fused Location Provider
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000); // Update every 5 seconds
            locationRequest.setFastestInterval(2000);

            // Use LocationCallback instead of LocationListener
            com.google.android.gms.location.LocationCallback locationCallback =
                    new com.google.android.gms.location.LocationCallback() {
                        @Override
                        public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                            if (locationResult != null && locationResult.getLastLocation() != null) {
                                currentLocation = locationResult.getLastLocation();
                            }
                        }
                    };

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            // Get last known location immediately
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    currentLocation = location;
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void handleSOS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        String sosPhone = prefManager.getSosContactPhone();

        if (sosPhone.isEmpty()) {
            Toast.makeText(this, "Please set SOS contact first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get fresh location before sending SOS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLocation = location;
                    }
                    sendSOSMessage(sosPhone);
                });
            } catch (Exception e) {
                e.printStackTrace();
                sendSOSMessage(sosPhone);
            }
        } else {
            sendSOSMessage(sosPhone);
        }
    }

    private void sendSOSMessage(String sosPhone) {
        String sosMessage = getString(R.string.sos_message);
        String locationLink = "";

        if (currentLocation != null) {
            locationLink = "https://maps.google.com/?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        } else {
            locationLink = "(Location not available at this moment)";
        }

        String fullMessage = sosMessage + " " + locationLink;

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + sosPhone));
        intent.putExtra("sms_body", fullMessage);
        startActivity(intent);
    }

    private void dialHelpline() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1091"));
        startActivity(intent);
    }

    private void shareLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        // Get fresh location
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    currentLocation = location;
                    sendLocationViaIntent();
                } else {
                    Toast.makeText(DashboardActivity.this,
                            "Getting location... Please try again",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Location error", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendLocationViaIntent() {
        if (currentLocation == null) {
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        String locationLink = "https://maps.google.com/?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        String locationMessage = "Check my current location: " + locationLink;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, locationMessage);
        startActivity(Intent.createChooser(shareIntent, "Share Location via"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}