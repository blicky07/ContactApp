package com.example.contactapp.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import java.io.ByteArrayOutputStream;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.contactapp.Contact.Contacts;
import com.example.contactapp.Database.ContactDatabaseHelper;
import com.example.contactapp.R;
public class UpdateContactFragment extends Fragment {

    private EditText nameEditText, phoneEditText, emailEditText;
    private String contactName;
    private ImageView profileImageView;
    private byte[] profileImage;
    private ActivityResultLauncher<Intent> mGetContent;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> mGetGalleryContent;  // New ActivityResultLauncher for gallery


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register for gallery intent
        mGetGalleryContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                                profileImageView.setImageBitmap(imageBitmap);

                                // Convert the image to a byte array
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                profileImage = stream.toByteArray();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        profileImageView.setImageBitmap(imageBitmap);

                        // Convert the image to a byte array
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        profileImage = stream.toByteArray();
                    }
                }
        );

        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mGetContent.launch(takePictureIntent);
            } else {
                Toast.makeText(getActivity(), "Camera permission is required to capture image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_contact, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        profileImageView = view.findViewById(R.id.profileImageView);  // Initialize this before setting the image

        // Fetch and display the contact details
        Bundle bundle = getArguments();
        if (bundle != null) {
            contactName = bundle.getString("contactName");
            ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
            Contacts contact = db.getContactByName(contactName);
            if (contact != null) {
                nameEditText.setText(contact.getName());
                phoneEditText.setText(contact.getPhone());
                emailEditText.setText(contact.getEmail());

                // Retrieve the photo
                profileImage = contact.getPhoto();

                // Convert byte array back to Bitmap
                if (profileImage != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.length);
                    profileImageView.setImageBitmap(bitmap);  // Set the image to ImageView
                }
            }
        }

        // Button to launch gallery
        Button galleryButton = view.findViewById(R.id.updateContactGalleryButton);  // This ID should match the one in your layout
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                mGetGalleryContent.launch(intent);
            }
        });

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



        profileImageView = view.findViewById(R.id.profileImageView);  // Add this ImageView to your layout
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mGetContent.launch(takePictureIntent);
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                }
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

                updatedContact.setPhoto(profileImage);

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
