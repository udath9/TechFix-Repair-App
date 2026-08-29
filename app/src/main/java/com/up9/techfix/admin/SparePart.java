package com.up9.techfix.admin;

public class SparePart {

    private int id;
    private String name;
    private String category;
    private String partNumber;
    private int quantity;
    private int minimumStock;
    private double unitPrice;
    private String supplier;
    private String description;
    private String imageUri;

    public SparePart(
            int id,
            String name,
            String category,
            String partNumber,
            int quantity,
            int minimumStock,
            double unitPrice,
            String supplier,
            String description,
            String imageUri
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.partNumber = partNumber;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
        this.description = description;
        this.imageUri = imageUri;
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

    public String getPartNumber() {
        return partNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMinimumStock() {
        return minimumStock;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMinimumStock(int minimumStock) {
        this.minimumStock = minimumStock;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public boolean isLowStock() {
        return quantity <= minimumStock;
    }
}