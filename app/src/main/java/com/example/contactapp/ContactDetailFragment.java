package com.example.contactapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

public class ContactDetailFragment extends Fragment {

    private EditText nameEditText, phoneEditText, emailEditText;
    private Button editButton, saveButton, deleteButton;

    public ContactDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.addNameEditText);
        phoneEditText = view.findViewById(R.id.addPhoneEditText);
        emailEditText = view.findViewById(R.id.addEmailEditText);
        editButton = view.findViewById(R.id.editButton);
        saveButton = view.findViewById(R.id.saveButton);
        deleteButton = view.findViewById(R.id.deleteButton);


        // Set onClick listeners for buttons (you can add your logic here)

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the data from the EditText fields
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Create a new Contact object with the data
                Contacts newContact = new Contacts(0, name, phone, email, null);

                // Call the method in ContactListFragment to add the contact to the list
                if (getActivity() != null) {
                    ContactListFragment contactListFragment = (ContactListFragment) getActivity()
                            .getSupportFragmentManager()
                            .findFragmentById(R.id.contactListFragment); // Use the correct ID
                    if (contactListFragment != null) {
                        contactListFragment.addContactToList(newContact);
                    }
                }

                // Clear the EditText fields after saving
                nameEditText.setText("");
                phoneEditText.setText("");
                emailEditText.setText("");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
            }
        });

        return view;
    }
}
