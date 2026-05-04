package com.example.raksha.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "RakshaPreferences";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_SOS_SETUP = "sos_setup";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_SOS_CONTACT_NAME = "sos_contact_name";
    private static final String KEY_SOS_CONTACT_PHONE = "sos_contact_phone";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        editor.putBoolean(KEY_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void setUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    public String getUserPhone() {
        return sharedPreferences.getString(KEY_USER_PHONE, "");
    }

    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public void setSosSetup(boolean isSetup) {
        editor.putBoolean(KEY_SOS_SETUP, isSetup);
        editor.apply();
    }

    public boolean isSosSetup() {
        return sharedPreferences.getBoolean(KEY_SOS_SETUP, false);
    }

    public void setDarkMode(boolean isDarkMode) {
        editor.putBoolean(KEY_DARK_MODE, isDarkMode);
        editor.apply();
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }

    public void setLanguage(String language) {
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGE, "en");
    }

    public void setSosContactName(String name) {
        editor.putString(KEY_SOS_CONTACT_NAME, name);
        editor.apply();
    }

    public String getSosContactName() {
        return sharedPreferences.getString(KEY_SOS_CONTACT_NAME, "");
    }

    public void setSosContactPhone(String phone) {
        editor.putString(KEY_SOS_CONTACT_PHONE, phone);
        editor.apply();
    }

    public String getSosContactPhone() {
        return sharedPreferences.getString(KEY_SOS_CONTACT_PHONE, "");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}