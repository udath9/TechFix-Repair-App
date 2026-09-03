package com.up9.techfix.admin.categories;



public class DeviceCategory {

    private int id;
    private String name;
    private String description;
    private double priceModifier;

    public DeviceCategory(int id, String name, String description, double priceModifier) {
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

    public double getPriceModifier() {
        return priceModifier;
    }

    public void setPriceModifier(double priceModifier) {
        this.priceModifier = priceModifier;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}