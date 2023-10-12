package com.example.contactapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.contactapp.Contact.Contacts;
import com.example.contactapp.Database.ContactDatabaseHelper;
import com.example.contactapp.R;

public class AddContactFragment extends Fragment {

    private EditText addNameEditText, addPhoneEditText, addEmailEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);

        addNameEditText = view.findViewById(R.id.addNameEditText);
        addPhoneEditText = view.findViewById(R.id.addPhoneEditText);
        addEmailEditText = view.findViewById(R.id.addEmailEditText);

        Button saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
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

    private void saveContact() {
        String name = addNameEditText.getText().toString();
        String phone = addPhoneEditText.getText().toString();
        String email = addEmailEditText.getText().toString();

        Contacts newContact = new Contacts();
        newContact.setName(name);
        newContact.setPhone(phone);
        newContact.setEmail(email);

        ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
        db.addContact(newContact);
    }

    private void goBackToContactList() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
