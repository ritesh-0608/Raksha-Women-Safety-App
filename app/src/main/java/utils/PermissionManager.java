package com.example.raksha.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    private static final int PERMISSION_REQUEST_CODE = 100;

    public static boolean checkLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public static boolean checkContactPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestContactPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public static boolean checkSmsPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestSmsPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public static boolean checkCallPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestCallPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    public static void requestAllPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.CALL_PHONE
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }
}