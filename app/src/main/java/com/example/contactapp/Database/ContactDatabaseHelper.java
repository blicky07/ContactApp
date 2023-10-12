package com.example.contactapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactapp.Contact.ContactContract;
import com.example.contactapp.Contact.Contacts;

import java.util.ArrayList;
import java.util.List;

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

    public List<Contacts> getAllContacts() {
        List<Contacts> contactsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_NAME,
                ContactContract.ContactEntry.COLUMN_PHONE,
                ContactContract.ContactEntry.COLUMN_EMAIL,
                ContactContract.ContactEntry.COLUMN_PHOTO
        };

        Cursor cursor = db.query(
                ContactContract.ContactEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,          // The columns for the WHERE clause
                null,       // The values for the WHERE clause
                null,          // Don't group the rows
                null,           // Don't filter by row groups
                null            // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Contacts contact = new Contacts();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry._ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_NAME)));
                contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_PHONE)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_EMAIL)));
                byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_PHOTO));
                contact.setPhoto(photo);
                contactsList.add(contact);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return contactsList;
    }

    public int updateContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactContract.ContactEntry.COLUMN_PHONE, contact.getPhone());
        values.put(ContactContract.ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactContract.ContactEntry.COLUMN_PHOTO, contact.getPhoto());

        // Update the row and get the number of rows affected
        int rowsAffected = db.update(
                ContactContract.ContactEntry.TABLE_NAME,
                values,
                ContactContract.ContactEntry._ID + " = ?",
                new String[]{String.valueOf(contact.getId())}
        );

        db.close();
        return rowsAffected;
    }

    public long deleteContact(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the row and get the number of rows affected
        long rowsDeleted = db.delete(
                ContactContract.ContactEntry.TABLE_NAME,
                ContactContract.ContactEntry._ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        db.close();

        return rowsDeleted;
    }

    public Contacts getContactByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                ContactContract.ContactEntry._ID,
                ContactContract.ContactEntry.COLUMN_NAME,
                ContactContract.ContactEntry.COLUMN_PHONE,
                ContactContract.ContactEntry.COLUMN_EMAIL,
                ContactContract.ContactEntry.COLUMN_PHOTO
        };

        // Query to fetch the contact with the given name
        Cursor cursor = db.query(
                ContactContract.ContactEntry.TABLE_NAME,
                projection,
                ContactContract.ContactEntry.COLUMN_NAME + " = ?",
                new String[]{name},
                null,
                null,
                null,
                "1"  // Limit to 1 result
        );

        Contacts contact = null;
        if (cursor != null && cursor.moveToFirst()) {
            contact = new Contacts();
            contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry._ID)));
            contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_NAME)));
            contact.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_PHONE)));
            contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_EMAIL)));
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_PHOTO));
            contact.setPhoto(photo);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return contact;
    }




    // Other CRUD methods (e.g., update, delete, etc.)
}
