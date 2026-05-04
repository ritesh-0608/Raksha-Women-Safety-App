package com.example.raksha;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.raksha.db.RakshaDBHelper;
import com.example.raksha.utils.PermissionManager;
import com.example.raksha.utils.SharedPrefManager;

public class SosContactSetupActivity extends AppCompatActivity {

    private static final int PICK_CONTACT_REQUEST = 1;
    private TextView selectedContactText;
    private Button selectContactBtn, proceedBtn, skipBtn;
    private String selectedContactName = "";
    private String selectedContactPhone = "";
    private RakshaDBHelper dbHelper;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_setup);

        dbHelper = new RakshaDBHelper(this);
        prefManager = new SharedPrefManager(this);

        selectedContactText = findViewById(R.id.selected_contact_text);
        selectContactBtn = findViewById(R.id.select_contact_btn);
        proceedBtn = findViewById(R.id.proceed_btn);
        skipBtn = findViewById(R.id.skip_btn);

        selectContactBtn.setOnClickListener(v -> selectContact());
        proceedBtn.setOnClickListener(v -> handleProceed());
        skipBtn.setOnClickListener(v -> skipSosSetup());

        PermissionManager.requestContactPermission(this);
    }

    private void selectContact() {
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
            selectedContactText.setText("✓ " + selectedContactName + "\n" + selectedContactPhone);
            phoneCursor.close();
        }
    }

    private void handleProceed() {
        if (selectedContactName.isEmpty() || selectedContactPhone.isEmpty()) {
            Toast.makeText(this, getString(R.string.contact_not_selected), Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = prefManager.getUserId();
        if (dbHelper.addSosContact(userId, selectedContactName, selectedContactPhone)) {
            prefManager.setSosSetup(true);
            prefManager.setSosContactName(selectedContactName);
            prefManager.setSosContactPhone(selectedContactPhone);
            Toast.makeText(this, "SOS contact saved successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SosContactSetupActivity.this, DashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Failed to save SOS contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void skipSosSetup() {
        prefManager.setSosSetup(true);
        startActivity(new Intent(SosContactSetupActivity.this, DashboardActivity.class));
        finish();
    }
}