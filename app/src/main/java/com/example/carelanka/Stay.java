package com.example.carelanka;

public class Stay {
    public String name;
    public String location;
    public String price;
    public String rating;
    public String description;
    public String phone;
    public String imageUrl;

    public Stay() {} // Firebase සඳහා

    public Stay(String name, String location, String price, String rating, String description, String phone) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.rating = rating;
        this.description = description;
        this.phone = phone;
        this.imageUrl = "";
    }
}
