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

    // Define the table and columns
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactContract.ContactEntry.TABLE_NAME + " (" +
                    ContactContract.ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactContract.ContactEntry.COLUMN_NAME + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_PHONE + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_EMAIL + " TEXT," +
                    ContactContract.ContactEntry.COLUMN_PHOTO + " BLOB)";

    // Constructor
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
        // Typically, you would perform migrations or recreate the database
    }

    // CRUD Operations

    // Add a new contact to the database
    public long addContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactContract.ContactEntry.COLUMN_PHONE, contact.getPhone());
        values.put(ContactContract.ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactContract.ContactEntry.COLUMN_PHOTO, contact.getPhoto()); // Store photo as a byte array (BLOB)

        try {
            long newRowId = db.insertOrThrow(ContactContract.ContactEntry.TABLE_NAME, null, values);
            return newRowId;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error
        } finally {
            db.close();
        }
    }

    // Get a specific contact by ID
    public Contacts getContact(long contactId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    ContactContract.ContactEntry.TABLE_NAME,
                    null,
                    ContactContract.ContactEntry._ID + " = ?",
                    new String[]{String.valueOf(contactId)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                return cursorToContact(cursor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return null;
    }

    // Helper method to convert a cursor to a Contact object
    private Contacts cursorToContact(Cursor cursor) {
        Contacts contact = new Contacts();
        int idIndex = cursor.getColumnIndexOrThrow(ContactContract.ContactEntry._ID);
        int nameIndex = cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_NAME);
        int phoneIndex = cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_PHONE);
        int emailIndex = cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_EMAIL);
        int photoIndex = cursor.getColumnIndexOrThrow(ContactContract.ContactEntry.COLUMN_PHOTO);

        contact.setId(cursor.getLong(idIndex));
        contact.setName(cursor.getString(nameIndex));
        contact.setPhone(cursor.getString(phoneIndex));
        contact.setEmail(cursor.getString(emailIndex));
        contact.setPhoto(cursor.getBlob(photoIndex));
        return contact;
    }

    // Update an existing contact
    public int updateContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContactContract.ContactEntry.COLUMN_NAME, contact.getName());
        values.put(ContactContract.ContactEntry.COLUMN_PHONE, contact.getPhone());
        values.put(ContactContract.ContactEntry.COLUMN_EMAIL, contact.getEmail());
        values.put(ContactContract.ContactEntry.COLUMN_PHOTO, contact.getPhoto());

        try {
            return db.update(
                    ContactContract.ContactEntry.TABLE_NAME,
                    values,
                    ContactContract.ContactEntry._ID + " = ?",
                    new String[]{String.valueOf(contact.getId())}
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error
        } finally {
            db.close();
        }
    }

    // Delete a contact by ID
    public void deleteContact(long contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(
                    ContactContract.ContactEntry.TABLE_NAME,
                    ContactContract.ContactEntry._ID + " = ?",
                    new String[]{String.valueOf(contactId)}
            );
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
