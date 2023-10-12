package com.example.contactapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

import com.example.contactapp.*;
import com.example.contactapp.Contact.*;
import com.example.contactapp.Database.ContactDatabaseHelper;

public class ContactCreateFragment extends Fragment {

    private EditText nameEditText, phoneEditText, emailEditText;
    private Button createButton;
    Contacts newContact;

    public ContactCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_contact, container, false);

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.createNameEditText);
        phoneEditText = view.findViewById(R.id.createPhoneEditText);
        emailEditText = view.findViewById(R.id.createEmailEditText);
        createButton = view.findViewById(R.id.createButton);

        // Set onClick listeners for buttons
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle create button click
                // Get the data from the EditText fields
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Create a new Contact object with the new data
                newContact = new Contacts(0, name, phone, email, null);

                // Add the contact to the database
                long newContactId = addContactToDatabase(newContact);

                if (newContactId > 0) {
                    // Successful creation. You might want to do something here like navigating back to the list
                    // or clearing the input fields.

                    nameEditText.setText("");
                    phoneEditText.setText("");
                    emailEditText.setText("");
                } else {
                    nameEditText.setText("An ERROR occurred");
                }
            }
        });

        return view;
    }

    // Database helper methods
    private long addContactToDatabase(Contacts contact) {
        ContactDatabaseHelper dbHelper = new ContactDatabaseHelper(requireContext());
        return dbHelper.addContact(contact);
    }
}
