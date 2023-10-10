package com.example.contactapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Create an instance of your adapter (ContactListAdapter) and set it to the RecyclerView
        adapter = new ContactListAdapter();
        recyclerView.setAdapter(adapter);

        // Load your contact data into the adapter
        // You need to implement the logic to populate your contact data into the adapter here

        return view;
    }

    // Add any other methods or logic related to your ContactListFragment here
}

public void addContactToList(Contacts contact) {
        // Add the contact to your contactList (the list you use for the RecyclerView)
        // You need to implement the logic to add the contact to your list here
        // For example, contactList.add(contact);
        // Then, update the RecyclerView by calling contactAdapter.notifyDataSetChanged();
        }