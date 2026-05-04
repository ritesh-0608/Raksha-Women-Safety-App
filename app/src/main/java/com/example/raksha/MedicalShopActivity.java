package com.example.raksha;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MedicalShopActivity extends AppCompatActivity {

    private Button mapsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_shop);

        mapsBtn = findViewById(R.id.maps_btn);
        mapsBtn.setOnClickListener(v -> openMedicalShops());
    }

    private void openMedicalShops() {
        String query = "medical shop pharmacy near me";
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + query);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
        } catch (Exception e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/maps?q=" + query));
            startActivity(browserIntent);
        }
    }
}