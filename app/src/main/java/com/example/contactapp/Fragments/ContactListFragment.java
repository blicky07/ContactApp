package com.example.contactapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Contact.ContactListAdapter;
import com.example.contactapp.Contact.Contacts;
import com.example.contactapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactListAdapter adapter;
    List<Contacts> contactList = getContactData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create an instance of your adapter (ContactListAdapter) and set it to the RecyclerView
        adapter = new ContactListAdapter(contactList);
        recyclerView.setAdapter(adapter);

        // Load your contact data into the adapter
        // You need to implement the logic to populate your contact data into the adapter here
        List<Contacts> contactData = getContactData();
        adapter.setContacts(contactData);

        return view;
    }

    // Example method to fetch contact data (replace with your own implementation)
    private List<Contacts> getContactData() {
        List<Contacts> contactData = new ArrayList<>();
        // Populate the contactData list with contact objects
        // Example: contactData.add(new Contacts("John Doe", "123-456-7890", "john@example.com"));
        // Repeat for all contacts you want to display
        return contactData;
    }


}
