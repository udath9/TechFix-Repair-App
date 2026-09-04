package com.up9.techfix.data;

public class Category {

    private int id;
    private String name;
    private String description;
    private double priceModifier;

    public Category(
            int id,
            String name,
            String description,
            double priceModifier
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceModifier = priceModifier;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPriceModifier() {
        return priceModifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriceModifier(double priceModifier) {
        this.priceModifier = priceModifier;
    }
}