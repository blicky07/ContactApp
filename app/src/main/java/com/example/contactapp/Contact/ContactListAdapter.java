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

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private List<Contacts> contactList;

    public interface OnContactClickListener {
        void onContactClick(int position);
    }

    private OnContactClickListener listener;

    public void setOnContactClickListener(OnContactClickListener listener) {
        this.listener = listener;
    }

    public ContactListAdapter(List<Contacts> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacts contact = contactList.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setContacts(List<Contacts> contacts) {
        this.contactList = contacts;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photoImageView;
        private TextView nameTextView;
        private TextView phoneTextView;
        private TextView emailTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onContactClick(position);
                        }
                    }
                }
            });

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
                photoImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }
    }
}
