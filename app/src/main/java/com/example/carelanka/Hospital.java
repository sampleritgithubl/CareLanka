package com.example.carelanka;

public class Hospital {
    public String name;
    public String address;
    public String district;
    public String type; // "Government" හෝ "Private"

    public Hospital() {} // Firebase සඳහා අනිවාර්යයි

    public Hospital(String name, String address, String district, String type) {
        this.name = name;
        this.address = address;
        this.district = district;
        this.type = type;
    }
}