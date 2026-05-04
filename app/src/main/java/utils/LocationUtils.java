package com.example.raksha.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class LocationUtils {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location currentLocation;
    private static final int MIN_TIME = 1000; // 1 second
    private static final int MIN_DISTANCE = 0; // 0 meters

    public LocationUtils(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationUpdates(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLocationUpdates() {
        if (locationListener != null && locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    public String getLocationLink() {
        if (currentLocation != null) {
            return "https://maps.google.com/?q=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        }
        return "";
    }

    public Location getCurrentLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        return null;
    }
}