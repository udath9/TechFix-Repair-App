package com.up9.techfix.admin;

public class SparePart {

    private int id;
    private String partName;
    private String partCode;
    private String category;
    private int quantity;
    private double unitPrice;
    private String supplier;

    public SparePart(
            int id,
            String partName,
            String partCode,
            String category,
            int quantity,
            double unitPrice,
            String supplier
    ) {
        this.id = id;
        this.partName = partName;
        this.partCode = partCode;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public String getPartName() {
        return partName;
    }

    public String getPartCode() {
        return partCode;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }
}