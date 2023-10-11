package com.example.contactapp.Contact;

import android.provider.BaseColumns;

public final class ContactContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private ContactContract() {
    }

    /* Inner class that defines the table contents */
    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHOTO = "photo";
    }
}
