package com.up9.techfix.data;

public class Repair {

    private int id;
    private int customerId;
    private int categoryId;
    private String deviceModel;
    private int serviceId;
    private String problemDescription;
    private int branchId;
    private String imageUri;
    private String inProgressPhotoUri;
    private Integer assignedTechnicianId;
    private String status;
    private String repairDate;

    public Repair(
            int id,
            int customerId,
            int categoryId,
            String deviceModel,
            int serviceId,
            String problemDescription,
            int branchId,
            String imageUri,
            String inProgressPhotoUri,
            Integer assignedTechnicianId,
            String status,
            String repairDate
    ) {
        this.id = id;
        this.customerId = customerId;
        this.categoryId = categoryId;
        this.deviceModel = deviceModel;
        this.serviceId = serviceId;
        this.problemDescription = problemDescription;
        this.branchId = branchId;
        this.imageUri = imageUri;
        this.inProgressPhotoUri = inProgressPhotoUri;
        this.assignedTechnicianId = assignedTechnicianId;
        this.status = status;
        this.repairDate = repairDate;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public int getBranchId() {
        return branchId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getInProgressPhotoUri() {
        return inProgressPhotoUri;
    }

    public Integer getAssignedTechnicianId() {
        return assignedTechnicianId;
    }

    public String getStatus() {
        return status;
    }

    public String getRepairDate() {
        return repairDate;
    }
}