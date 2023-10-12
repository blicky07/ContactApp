package com.example.contactapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contactapp.Contact.Contacts;
import com.example.contactapp.Database.ContactDatabaseHelper;
import com.example.contactapp.R;

public class UpdateContactFragment extends Fragment {

    private EditText nameEditText, phoneEditText, emailEditText;
    private String contactName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_contact, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);

        // Fetch and display the contact details
        Bundle bundle = getArguments();
        if (bundle != null) {
            contactName = bundle.getString("contactName");  // Get the name passed from the previous fragment
            ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
            Contacts contact = db.getContactByName(contactName);  // Assuming you've implemented this method
            if (contact != null) {
                nameEditText.setText(contact.getName());
                phoneEditText.setText(contact.getPhone());
                emailEditText.setText(contact.getEmail());
            }
        }



        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContact();
            }
        });

        Button deleteButton = view.findViewById(R.id.deleteButton); // Add this button in your layout
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        Button backButton = view.findViewById(R.id.backNewContact);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToContactList();
            }
        });

        return view;
    }

    // Edit/Save Changes
    private void updateContact() {
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
        // Fetch and display the contact details
        Bundle bundle = getArguments();
        if (bundle != null) {
            contactName = bundle.getString("contactName");  // Get the name passed from the previous fragment
            Contacts contact = db.getContactByName(contactName);  // Assuming you've implemented this method
            if (contact != null) {
                Contacts updatedContact = new Contacts(contact);

                updatedContact.setName(name);
                updatedContact.setPhone(phone);
                updatedContact.setEmail(email);

                int rowsAffected = db.updateContact(updatedContact); // Implement this method in your database helper

                if (rowsAffected > 0) {
                    Toast.makeText(getActivity(), "Contact updated successfully!", Toast.LENGTH_SHORT).show();
                    goBackToContactList();
                } else {
                    Toast.makeText(getActivity(), "Failed to update contact", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void deleteContact() {
        ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
        // Fetch and display the contact details
        Bundle bundle = getArguments();
        if (bundle != null) {
            contactName = bundle.getString("contactName");  // Get the name passed from the previous fragment
            Contacts contact = db.getContactByName(contactName);  // Assuming you've implemented this method
            if (contact != null) {
                long rowsDeleted = db.deleteContact(contact.getId());
                if (rowsDeleted > 0) {
                    Toast.makeText(getActivity(), "Contact deleted successfully!", Toast.LENGTH_SHORT).show();
                    goBackToContactList();
                } else {
                    Toast.makeText(getActivity(), "Failed to delete contact", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


    private void goBackToContactList() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
