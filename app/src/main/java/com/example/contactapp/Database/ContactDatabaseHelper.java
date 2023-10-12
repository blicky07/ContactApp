package com.example.contactapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactapp.Contact.ContactContract;
import com.example.contactapp.Contact.Contacts;

public class ContactDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactManager.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactContract.ContactEntry.TABLE_NAME + " (" +
                    ContactContract.ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactContract.ContactEntry.COLUMN_NAME + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_PHONE + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_EMAIL + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_PHOTO + " BLOB)";

    public ContactDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_ENTRIES);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here, if needed
    }

    public long addContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactContract.ContactEntry.COLUMN_PHONE, contact.getPhone());
        values.put(ContactContract.ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactContract.ContactEntry.COLUMN_PHOTO, contact.getPhoto());

        long newRowId = -1;
        try {
            newRowId = db.insertOrThrow(ContactContract.ContactEntry.TABLE_NAME, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return newRowId;
    }
    // Other CRUD methods (e.g., update, delete, etc.)
}
