package com.example.carelanka;

public class User {
    public String uid, name, email, phone, role;

    public User() {
        // Firebase සඳහා අවශ්‍ය හිස් Constructor එකකි
    }

    public User(String uid, String name, String email, String phone, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}