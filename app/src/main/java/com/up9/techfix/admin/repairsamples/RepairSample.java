package com.up9.techfix.admin.repairsamples;

public class RepairSample {

    private int id;
    private String deviceName;
    private String category;
    private String service;
    private String description;
    private String imageUri;

    public RepairSample(
            int id,
            String deviceName,
            String category,
            String service,
            String description,
            String imageUri
    ) {
        this.id = id;
        this.deviceName = deviceName;
        this.category = category;
        this.service = service;
        this.description = description;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getCategory() {
        return category;
    }

    public String getService() {
        return service;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}