package com.example.contactapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.Contact.ContactListAdapter;
import com.example.contactapp.Contact.Contacts;
import com.example.contactapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private ContactListAdapter adapter;
    List<Contacts> contactList = getContactData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        // Initialize RecyclerView and EmptyView
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyView = view.findViewById(R.id.emptyView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        adapter = new ContactListAdapter(contactList);
        recyclerView.setAdapter(adapter);

        // Load your contact data into the adapter
        List<Contacts> contactData = getContactData();
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

    // Example method to fetch contact data (replace with your own implementation)
    private List<Contacts> getContactData() {
        List<Contacts> contactData = new ArrayList<>();
        // Populate the contactData list with contact objects
        // Example: contactData.add(new Contacts("John Doe", "123-456-7890", "john@example.com"));
        // Repeat for all contacts you want to display
        return contactData;
    }
}
