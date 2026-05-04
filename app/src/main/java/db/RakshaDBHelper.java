package com.example.raksha.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RakshaDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "raksha_db";
    private static final int DATABASE_VERSION = 1;

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_OCCUPATION = "occupation";
    private static final String COLUMN_PASSWORD = "password";

    // SOS Contacts Table
    private static final String TABLE_SOS_CONTACTS = "sos_contacts";
    private static final String COLUMN_SOS_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_CONTACT_NAME = "contact_name";
    private static final String COLUMN_CONTACT_PHONE = "contact_phone";

    // Safety Messages Table
    private static final String TABLE_SAFETY_MESSAGES = "safety_messages";
    private static final String COLUMN_MESSAGE_ID = "id";
    private static final String COLUMN_MESSAGE = "message";
    private static final String COLUMN_CATEGORY = "category";

    public RakshaDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_PHONE + " TEXT UNIQUE NOT NULL,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_OCCUPATION + " TEXT,"
                + COLUMN_PASSWORD + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Create SOS Contacts Table
        String CREATE_SOS_TABLE = "CREATE TABLE " + TABLE_SOS_CONTACTS + "("
                + COLUMN_SOS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER NOT NULL,"
                + COLUMN_CONTACT_NAME + " TEXT NOT NULL,"
                + COLUMN_CONTACT_PHONE + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")" + ")";
        db.execSQL(CREATE_SOS_TABLE);

        // Create Safety Messages Table
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_SAFETY_MESSAGES + "("
                + COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MESSAGE + " TEXT NOT NULL,"
                + COLUMN_CATEGORY + " TEXT" + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);

        // Insert default safety messages
        insertDefaultSafetyMessages(db);
    }

    private void insertDefaultSafetyMessages(SQLiteDatabase db) {
        String[] messages = {
                "Always share your location with trusted contacts",
                "Keep your phone battery charged",
                "Be aware of your surroundings",
                "Trust your instincts",
                "Keep emergency numbers saved",
                "Use well-lit routes",
                "Inform someone about your travel plans"
        };

        for (String message : messages) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MESSAGE, message);
            values.put(COLUMN_CATEGORY, "Safety Tip");
            db.insert(TABLE_SAFETY_MESSAGES, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOS_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAFETY_MESSAGES);
        onCreate(db);
    }

    // User Operations
    public boolean addUser(String name, String phone, int age, String occupation, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_OCCUPATION, occupation);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_PHONE + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{phone, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public int getUserId(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID}, COLUMN_PHONE + "=?",
                new String[]{phone}, null, null, null);
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    public Cursor getUserData(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, null);
    }

    public boolean updateUser(int userId, String name, int age, String occupation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_OCCUPATION, occupation);

        long result = db.update(TABLE_USERS, values, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    // SOS Contact Operations
    public boolean addSosContact(int userId, String contactName, String contactPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_CONTACT_NAME, contactName);
        values.put(COLUMN_CONTACT_PHONE, contactPhone);

        long result = db.insert(TABLE_SOS_CONTACTS, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateSosContact(int userId, String contactName, String contactPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NAME, contactName);
        values.put(COLUMN_CONTACT_PHONE, contactPhone);

        long result = db.update(TABLE_SOS_CONTACTS, values, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    public Cursor getSosContact(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_SOS_CONTACTS, null, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, null);
    }

    // Safety Messages
    public Cursor getAllSafetyMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_SAFETY_MESSAGES, null, null, null, null, null, null);
    }

    public boolean checkPhoneExists(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_PHONE + "=?", new String[]{phone},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}