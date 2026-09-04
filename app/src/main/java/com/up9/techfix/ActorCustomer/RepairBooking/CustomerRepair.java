package com.up9.techfix.ActorCustomer.RepairBooking;

public class CustomerRepair {

    private int repairId;

    private String categoryName;
    private String deviceModel;
    private String serviceName;
    private String branchName;
    private String technicianName;
    private String status;
    private String repairDate;

    private double finalPrice;

    public CustomerRepair(
            int repairId,
            String categoryName,
            String deviceModel,
            String serviceName,
            String branchName,
            String technicianName,
            String status,
            String repairDate,
            double finalPrice
    ) {

        this.repairId = repairId;
        this.categoryName = categoryName;
        this.deviceModel = deviceModel;
        this.serviceName = serviceName;
        this.branchName = branchName;
        this.technicianName = technicianName;
        this.status = status;
        this.repairDate = repairDate;
        this.finalPrice = finalPrice;
    }

    public int getRepairId() {
        return repairId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public String getStatus() {
        return status;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}