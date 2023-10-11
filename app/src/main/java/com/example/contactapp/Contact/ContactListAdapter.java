package com.example.contactapp.Contact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactapp.R;

import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{

    private List<Contacts> contactList;

    public List<Contacts> getContactList() {
        return contactList;
    }

    public ContactListAdapter(List<Contacts> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout and create a ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind your contact data to the ViewHolder's views
        Contacts contact = contactList.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setContacts(List<Contacts> contacts) {
        this.contactList = contacts;
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoImageView;
        private TextView nameTextView;
        private TextView phoneTextView;
        private TextView emailTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            photoImageView = itemView.findViewById(R.id.photoImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }

        public void bind(Contacts contact) {
            nameTextView.setText(contact.getName());
            phoneTextView.setText("Phone: " + contact.getPhone());
            emailTextView.setText("Email: " + contact.getEmail());

            if (contact.getPhoto() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getPhoto(), 0, contact.getPhoto().length);
                photoImageView.setImageBitmap(bitmap);
            } else {
                // Use a placeholder image if no photo is available
                photoImageView.setImageResource(R.drawable.ic_launcher_foreground); // Change this to your placeholder image
            }
        }
    }
}
