package com.example.contactapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import java.util.*;

import com.example.contactapp.Contact.Contacts;
import com.example.contactapp.Database.ContactDatabaseHelper;
import android.Manifest;
import com.example.contactapp.R;

public class AddContactFragment extends Fragment {

    private EditText addNameEditText, addPhoneEditText, addEmailEditText;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
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
                // Permission granted, launch the camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mGetContent.launch(takePictureIntent);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(getActivity(), "Camera permission is required to capture image", Toast.LENGTH_SHORT).show();
            }
        });
    }
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImageView = view.findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // You can use the camera
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mGetContent.launch(takePictureIntent);
                } else {
                    // Request permission
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                }
            }
        });

        // Button to launch gallery
        Button galleryButton = view.findViewById(R.id.addContactGalleryButton);  // Add this button to your layout
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                mGetGalleryContent.launch(intent);
            }
        });
    }

    private void saveContact() {
        String name = addNameEditText.getText().toString();
        String phone = addPhoneEditText.getText().toString();
        String email = addEmailEditText.getText().toString();


        Contacts newContact = new Contacts();
        newContact.setName(name);
        newContact.setPhone(phone);
        newContact.setEmail(email);
        newContact.setPhoto(profileImage);  // Store the byte array in the Contacts object

        ContactDatabaseHelper db = new ContactDatabaseHelper(getActivity());
        long newRowId = db.addContact(newContact);

        if (newRowId != -1) {
            // Contact added successfully
            // Show a toast or dialog to inform the user
            Toast.makeText(getActivity(), "Contact added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            // Failed to add contact
            // Show a toast or dialog to inform the user
            Toast.makeText(getActivity(), "Failed to add contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void goBackToContactList() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
