package com.example.contactapp.Fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;

import com.example.contactapp.*;
import com.example.contactapp.Contact.*;
import com.example.contactapp.Database.ContactDatabaseHelper;

public class ContactDetailFragment extends Fragment {

    private EditText nameEditText, phoneEditText, emailEditText;
    private Button editButton, saveButton, deleteButton;
    Contacts contact = new Contacts();
    private long contactId = 0;

    public ContactDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);

        contactId = contact.getId();

        // Initialize UI elements
        nameEditText = view.findViewById(R.id.addNameEditText);
        phoneEditText = view.findViewById(R.id.addPhoneEditText);
        emailEditText = view.findViewById(R.id.addEmailEditText);
        editButton = view.findViewById(R.id.editButton);
        saveButton = view.findViewById(R.id.saveButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        // Set onClick listeners for buttons
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
                // Enable editing of EditText fields
                nameEditText.setEnabled(true);
                phoneEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                // Show the "Save" button and hide the "Edit" button
                saveButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.GONE);
                deleteButton.setEnabled(false); // Disable delete button during edit
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle save button click
                // Get the data from the EditText fields
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();

                // Create a new Contact object with the edited data
                Contacts updatedContact = new Contacts(contactId, name, phone, email, null);

                // Update the contact in the database
                long rowsUpdated = updateContactInDatabase(updatedContact);

                if (rowsUpdated > 0) {
                    // Call the method in ContactListFragment to update the contact in the list
                    if (getActivity() != null) {
                        ContactListFragment contactListFragment = (ContactListFragment) getActivity()
                                .getSupportFragmentManager()
                                .findFragmentById(R.id.frameLayout2); // Use the correct ID
                        if (contactListFragment != null) {
                            updateContactInDatabase(updatedContact);
                        }
                    }

                    // Clear the EditText fields after saving
                    nameEditText.setText("");
                    phoneEditText.setText("");
                    emailEditText.setText("");

                    // Disable editing and show the "Edit" button
                    nameEditText.setEnabled(false);
                    phoneEditText.setEnabled(false);
                    emailEditText.setEnabled(false);
                    saveButton.setVisibility(View.GONE);
                    editButton.setVisibility(View.VISIBLE);
                    deleteButton.setEnabled(true); // Re-enable delete button
                } else {
                    // Handle the case where the update operation fails
                    // You can show an error message or perform appropriate error handling.
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                // Delete the contact from the database
                deleteContactFromDatabase(contactId);

                // Call the method in ContactListFragment to delete the contact from the list
                if (getActivity() != null) {
                    ContactListFragment contactListFragment = (ContactListFragment) getActivity()
                            .getSupportFragmentManager()
                            .findFragmentById(R.id.frameLayout2); // Use the correct ID
                    if (contactListFragment != null) {
                        deleteContactFromDatabase(contactId);
                    }
                }

                // Clear the EditText fields after deleting
                nameEditText.setText("");
                phoneEditText.setText("");
                emailEditText.setText("");
            }
        });

        return view;
    }

    // Database helper methods
    private long updateContactInDatabase(Contacts contact) {
        ContactDatabaseHelper dbHelper = new ContactDatabaseHelper(requireContext());
        return dbHelper.updateContact(contact);
    }

    private void deleteContactFromDatabase(long contactId) {
        ContactDatabaseHelper dbHelper = new ContactDatabaseHelper(requireContext());
        dbHelper.deleteContact(contactId);
    }
}
