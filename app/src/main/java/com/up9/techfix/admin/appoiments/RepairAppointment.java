package com.up9.techfix.admin.appoiments;

public class RepairAppointment {

    private int repairId;
    private int customerId;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    private String categoryName;
    private String deviceModel;
    private String serviceName;

    private String problemDescription;

    private String branchName;
    private String branchAddress;

    private int technicianId;
    private String technicianName;

    private String status;
    private String repairDate;
    private String appointmentTime;

    private double servicePrice;
    private double finalPrice;

    public RepairAppointment(
            int repairId,
            int customerId,
            String customerName,
            String customerEmail,
            String customerPhone,
            String categoryName,
            String deviceModel,
            String serviceName,
            String problemDescription,
            String branchName,
            String branchAddress,
            int technicianId,
            String technicianName,
            String status,
            String repairDate,
            double servicePrice,
            double finalPrice
    ) {

        this.repairId = repairId;
        this.customerId = customerId;

        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;

        this.categoryName = categoryName;
        this.deviceModel = deviceModel;
        this.serviceName = serviceName;

        this.problemDescription =
                problemDescription;

        this.branchName = branchName;
        this.branchAddress = branchAddress;

        this.technicianId = technicianId;
        this.technicianName = technicianName;

        this.status = status;

        this.repairDate = repairDate;

        this.servicePrice = servicePrice;
        this.finalPrice = finalPrice;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public int getRepairId() {
        return repairId;
    }

    public int getId() {
        return repairId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getDevice() {
        return deviceModel;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getService() {
        return serviceName;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getBranch() {
        return branchName;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public String getTechnician() {
        return technicianName;
    }

    public String getStatus() {
        return status;
    }

    public String getRepairDate() {
        return repairDate;
    }

    public String getAppointmentDate() {
        return repairDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getEstimatedPrice() {
        return finalPrice;
    }

    // =========================================================
    // SETTERS
    // =========================================================

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(
            String customerName
    ) {
        this.customerName = customerName;
    }

    public void setCustomerEmail(
            String customerEmail
    ) {
        this.customerEmail = customerEmail;
    }

    public void setCustomerPhone(
            String customerPhone
    ) {
        this.customerPhone = customerPhone;
    }

    public void setCategoryName(
            String categoryName
    ) {
        this.categoryName = categoryName;
    }

    public void setDeviceModel(
            String deviceModel
    ) {
        this.deviceModel = deviceModel;
    }

    public void setServiceName(
            String serviceName
    ) {
        this.serviceName = serviceName;
    }

    public void setProblemDescription(
            String problemDescription
    ) {
        this.problemDescription =
                problemDescription;
    }

    public void setBranchName(
            String branchName
    ) {
        this.branchName = branchName;
    }

    public void setBranch(
            String branch
    ) {
        this.branchName = branch;
    }

    public void setBranchAddress(
            String branchAddress
    ) {
        this.branchAddress =
                branchAddress;
    }

    public void setTechnicianId(
            int technicianId
    ) {
        this.technicianId =
                technicianId;
    }

    public void setTechnicianName(
            String technicianName
    ) {
        this.technicianName =
                technicianName;
    }

    public void setTechnician(
            String technician
    ) {
        this.technicianName =
                technician;
    }

    public void setStatus(
            String status
    ) {
        this.status = status;
    }

    public void setRepairDate(
            String repairDate
    ) {
        this.repairDate = repairDate;
    }

    public void setAppointmentTime(
            String appointmentTime
    ) {
        this.appointmentTime =
                appointmentTime;
    }

    public void setServicePrice(
            double servicePrice
    ) {
        this.servicePrice =
                servicePrice;
    }

    public void setFinalPrice(
            double finalPrice
    ) {
        this.finalPrice =
                finalPrice;
    }

    public void setEstimatedPrice(
            double estimatedPrice
    ) {

        this.finalPrice =
                estimatedPrice;

        this.servicePrice =
                estimatedPrice;
    }
}