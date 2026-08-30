package com.up9.techfix.data;

public class Branch {

    private int id;
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;

    public Branch(
            int id,
            String name,
            String address,
            String phone,
            double latitude,
            double longitude
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}