package com.up9.techfix.data;

public class Service {

    private int id;
    private String name;
    private String imageUri;
    private String description;
    private double price;
    private int estimatedDays;

    public Service(
            int id,
            String name,
            String imageUri,
            String description,
            double price,
            int estimatedDays
    ) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
        this.description = description;
        this.price = price;
        this.estimatedDays = estimatedDays;
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

    public int getEstimatedDays() {
        return estimatedDays;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }
}