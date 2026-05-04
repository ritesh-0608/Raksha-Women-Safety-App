package com.example.raksha;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.raksha.db.RakshaDBHelper;
import com.example.raksha.utils.SharedPrefManager;

public class SafetyTipsActivity extends AppCompatActivity {

    private LinearLayout tipsContainer;
    private RakshaDBHelper dbHelper;
    private SharedPrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new SharedPrefManager(this);
        if (prefManager.isDarkMode()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_safety_tips);

        dbHelper = new RakshaDBHelper(this);
        tipsContainer = findViewById(R.id.tips_container);

        loadSafetyTips();
    }

    private void loadSafetyTips() {
        Cursor cursor = dbHelper.getAllSafetyMessages();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
                addTipCard(message);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void addTipCard(String message) {
        TextView tipView = new TextView(this);
        tipView.setText("• " + message);
        tipView.setTextSize(14);
        tipView.setTextColor(getResources().getColor(R.color.accent_dark_gray));
        tipView.setPadding(16, 12, 16, 12);
        tipView.setLineSpacing(10, 1.6f);  // Changed from setLineSpacingMultiplier

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 8, 0, 8);
        tipView.setLayoutParams(params);

        tipsContainer.addView(tipView);
    }
}