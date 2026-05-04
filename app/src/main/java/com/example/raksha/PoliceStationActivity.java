package com.example.raksha;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PoliceStationActivity extends AppCompatActivity {

    private Button mapsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_station);

        mapsBtn = findViewById(R.id.maps_btn);
        mapsBtn.setOnClickListener(v -> openPoliceStations());
    }

    private void openPoliceStations() {
        String query = "police station near me";
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
        } catch (Exception e) {
            // If Google Maps is not installed, open in browser
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/maps?q=" + query));
            startActivity(browserIntent);
        }
    }
}