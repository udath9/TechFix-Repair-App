package com.up9.techfix.admin;

public class RepairService {

    private int id;
    private String name;
    private String category;
    private String description;
    private double price;
    private int estimatedDays;

    public RepairService(
            int id,
            String name,
            String category,
            String description,
            double price,
            int estimatedDays
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
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

    public String getCategory() {
        return category;
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

    public void setCategory(String category) {
        this.category = category;
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