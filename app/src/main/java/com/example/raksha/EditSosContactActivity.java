package com.example.raksha;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.example.raksha.db.RakshaDBHelper;
import com.example.raksha.utils.PermissionManager;
import com.example.raksha.utils.SharedPrefManager;

public class EditSosContactActivity extends AppCompatActivity {

    private static final int PICK_CONTACT_REQUEST = 1;

    private TextView contactNameDisplay, contactPhoneDisplay;
    private Button selectContactBtn, deleteContactBtn;
    private LinearLayout currentContactCard;
    private RakshaDBHelper dbHelper;
    private SharedPrefManager prefManager;

    private String selectedContactName = "";
    private String selectedContactPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new SharedPrefManager(this);
        if (prefManager.isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppThemeDark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_edit_sos_contact);

        dbHelper = new RakshaDBHelper(this);

        contactNameDisplay = findViewById(R.id.contact_name_display);
        contactPhoneDisplay = findViewById(R.id.contact_phone_display);
        selectContactBtn = findViewById(R.id.select_contact_btn);
        deleteContactBtn = findViewById(R.id.delete_contact_btn);
        currentContactCard = findViewById(R.id.current_contact_card);

        loadCurrentSosContact();
        selectContactBtn.setOnClickListener(v -> selectNewContact());
        deleteContactBtn.setOnClickListener(v -> deleteCurrentContact());

        PermissionManager.requestContactPermission(this);
    }

    private void loadCurrentSosContact() {
        int userId = prefManager.getUserId();
        Cursor cursor = dbHelper.getSosContact(userId);

        if (cursor != null && cursor.moveToFirst()) {
            selectedContactName = cursor.getString(cursor.getColumnIndexOrThrow("contact_name"));
            selectedContactPhone = cursor.getString(cursor.getColumnIndexOrThrow("contact_phone"));

            contactNameDisplay.setText(selectedContactName);
            contactPhoneDisplay.setText(selectedContactPhone);
            currentContactCard.setVisibility(android.view.View.VISIBLE);

            cursor.close();
        } else {
            currentContactCard.setVisibility(android.view.View.GONE);
            contactNameDisplay.setText("No SOS Contact Set");
            contactPhoneDisplay.setText("");
        }
    }

    private void selectNewContact() {
        if (!PermissionManager.checkContactPermission(this)) {
            PermissionManager.requestContactPermission(this);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri contactUri = data.getData();
            if (contactUri != null) {
                getContactDetails(contactUri);
            }
        }
    }

    private void getContactDetails(Uri contactUri) {
        Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);

            selectedContactName = cursor.getString(nameIndex);
            String contactId = cursor.getString(idIndex);

            getPhoneNumber(contactId);
            cursor.close();
        }
    }

    private void getPhoneNumber(String contactId) {
        Cursor phoneCursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null
        );

        if (phoneCursor != null && phoneCursor.moveToFirst()) {
            int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            selectedContactPhone = phoneCursor.getString(phoneIndex).replaceAll("[^0-9]", "");

            // Update UI with selected contact
            contactNameDisplay.setText(selectedContactName);
            contactPhoneDisplay.setText(selectedContactPhone);
            currentContactCard.setVisibility(android.view.View.VISIBLE);

            // Update database
            updateSosContactInDatabase();

            phoneCursor.close();
        }
    }

    private void updateSosContactInDatabase() {
        int userId = prefManager.getUserId();

        if (dbHelper.updateSosContact(userId, selectedContactName, selectedContactPhone)) {
            prefManager.setSosContactName(selectedContactName);
            prefManager.setSosContactPhone(selectedContactPhone);
            Toast.makeText(this, "SOS contact updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update SOS contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCurrentContact() {
        // Show confirmation dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete SOS Contact")
                .setMessage("Are you sure you want to delete this SOS contact?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int userId = prefManager.getUserId();
                    // Clear from database (set empty values)
                    if (dbHelper.updateSosContact(userId, "", "")) {
                        prefManager.setSosContactName("");
                        prefManager.setSosContactPhone("");
                        selectedContactName = "";
                        selectedContactPhone = "";
                        currentContactCard.setVisibility(android.view.View.GONE);
                        contactNameDisplay.setText("No SOS Contact Set");
                        contactPhoneDisplay.setText("");
                        Toast.makeText(EditSosContactActivity.this, "SOS contact deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}