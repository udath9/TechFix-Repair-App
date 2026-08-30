package com.up9.techfix.data;

public class Service {

    private int id;
    private String name;
    private String imageUri;
    private String description;
    private double price;

    public Service(
            int id,
            String name,
            String imageUri,
            String description,
            double price
    ) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
        this.description = description;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}