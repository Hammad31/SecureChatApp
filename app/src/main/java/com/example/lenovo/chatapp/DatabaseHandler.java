package com.example.lenovo.chatapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LENOVO on 10/23/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PrivateKey";

    //  table name
    private static final String TABLE_KEY = "PrivateKey";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY = "key";
    private static final String USER = "user";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_KEY + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + USER + " TEXT, " + KEY + " BLOB)";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEY);

        // Create tables again
        onCreate(db);
    }

    public void addPrivateKey(PrivateKey key) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY, key.key); // Save privateKey
        values.put(USER, key.username); // Save username

        // Inserting Row
        db.insert(TABLE_KEY, null, values);
        db.close(); // Closing database connection
    }

    public PrivateKey getPrivateKey(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_KEY, new String[]{KEY_ID, KEY, USER}, USER + "=?", new String[] { String.valueOf(username) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PrivateKey privateKey  = new PrivateKey(cursor.getBlob(2),cursor.getString(1));
        // return key
        return privateKey;
    }

    public List<PrivateKey> getAllKeys() {
        List<PrivateKey> list = new ArrayList<PrivateKey>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_KEY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PrivateKey privateKey  = new PrivateKey(cursor.getBlob(2),cursor.getString(1));
                // Adding a key to list
                list.add(privateKey);
            } while (cursor.moveToNext());
        }

        // return keys list
        return list;
    }
}
