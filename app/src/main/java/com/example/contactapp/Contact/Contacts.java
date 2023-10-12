package com.example.contactapp.Contact;

public class Contacts {
    private long id; // Use long for database ID
    private String name;
    private String phone;
    private String email;
    private byte[] photo; // Store photo as a byte array

    // Constructors
    public Contacts() {
        // Default constructor
    }

    public Contacts(long id, String name, String phone, String email, byte[] photo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
    }

    public Contacts(Contacts contact) {
        this.id = contact.id;
        this.name = contact.name;
        this.phone = contact.phone;
        this.email = contact.email;
        this.photo = contact.photo;
    }

    public Contacts(String name, String phone, String email, byte[] photo) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
    }


    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getPhoto() {
        return photo;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
