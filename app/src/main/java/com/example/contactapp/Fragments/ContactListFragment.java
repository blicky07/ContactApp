package com.example.contactapp.Fragments;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Contact.ContactContract;
import com.example.contactapp.Contact.ContactListAdapter;
import com.example.contactapp.Contact.Contacts;
import com.example.contactapp.Database.ContactDatabaseHelper;
import com.example.contactapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment implements ContactListAdapter.OnContactClickListener {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ContactListAdapter adapter;
    private static final int YOUR_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        // Initialize RecyclerView and EmptyView
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the Import Button
        Button importButton = view.findViewById(R.id.importButton);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int contactsAdded = 0;  // Initialize a counter for added contacts
                final int YOUR_REQUEST_CODE = 1;  // Define your request code for permission

                ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());  // Initialize your database helper

                // Check for permission
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

                    // Check if the column index is valid
                    if (cursor != null && cursor.getCount() > 0) {
                        int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                        if (nameColumnIndex != -1) {
                            // Loop through the cursor to read individual contacts
                            while (cursor.moveToNext()) {
                                String name = cursor.getString(nameColumnIndex);  // Use the column index directly

                                // Check if the contact already exists in your database
                                if (!db.checkForDuplicate(name)) {  // Note: You'll need to implement checkForDuplicate in your database helper
                                    db.addContact(new Contacts(name, "", "", (byte[]) null));  // Add the new contact
                                    contactsAdded++;  // Increment the counter
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to read contacts. Name column not found.", Toast.LENGTH_LONG).show();
                        }
                        cursor.close();
                    }
                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, YOUR_REQUEST_CODE);
                }

                // Refresh the contact list
                refreshContactList();

                // Show a Toast or dialog based on the value of contactsAdded
                if (contactsAdded > 0) {
                    Toast.makeText(getActivity(), contactsAdded + " contacts were added.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "No new contacts were added.", Toast.LENGTH_LONG).show();
                }
            }
        });


        // Initialize the Add New Contact Button
        Button addNewContactButton = view.findViewById(R.id.addNewContactButton);
        addNewContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new AddContactFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Create an instance of your adapter (ContactListAdapter) and set it to the RecyclerView
        ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
        List<Contacts> contactList = db.getAllContacts();
        adapter = new ContactListAdapter(contactList);
        adapter.setOnContactClickListener(this);
        recyclerView.setAdapter(adapter);

        // Load your contact data into the adapter
        List<Contacts> contactData = db.getAllContacts();
        adapter.setContacts(contactData);

        // Toggle visibility based on whether the list is empty or not
        if (contactList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return view;
    }

    private void refreshContactList() {
        // Reload the contact list from the database
        ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
        List<Contacts> newContactList = db.getAllContacts();

        // Update the adapter with the new list
        adapter.setContacts(newContactList);

        // Notify the RecyclerView that the data set has changed
        adapter.notifyDataSetChanged();
    }

    private void importContacts() {
        ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            // Loop through the cursor to read individual contact
            while (cursor.moveToNext()) {
                int idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                if (idIndex != -1 && nameIndex != -1) {
                    String contactId = cursor.getString(idIndex);
                    String name = cursor.getString(nameIndex);

                    // Use checkForDuplicate() to avoid adding duplicates
                    if (!db.checkForDuplicate(name)) {
                        db.addContact(new Contacts(name, "", "", (byte[]) null));  // Add the new contact
                    }
                }
            }
            cursor.close();

            // Update the RecyclerView with the new data
            List<Contacts> contactList = db.getAllContacts();
            adapter.setContacts(contactList);
            adapter.notifyDataSetChanged();
        }
    }


    // What happens if you click on any of the contacts
    @Override
    public void onContactClick(int position) {
        // Fetch the clicked contact based on position
        Contacts clickedContact = adapter.getContactAtPosition(position);
        String contactName = clickedContact.getName();

        // Create a Bundle and put the name of the clicked contact into it
        Bundle bundle = new Bundle();
        bundle.putString("contactName", contactName);

        // Create an instance of UpdateContactFragment and set its arguments
        UpdateContactFragment updateContactFragment = new UpdateContactFragment();
        updateContactFragment.setArguments(bundle);

        // Navigate to UpdateContactFragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, updateContactFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
