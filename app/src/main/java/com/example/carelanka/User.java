package com.example.carelanka;

public class User {
    public String uid, name, email, phone, role, price, rating, imageUrl;

    public User() {} // Firebase සඳහා

    public User(String uid, String name, String email, String phone, String role) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.price = "0"; // Default
        this.rating = "5.0"; // Default
        this.imageUrl = ""; // Default
    }
}