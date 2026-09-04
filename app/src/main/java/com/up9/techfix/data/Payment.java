package com.up9.techfix.data;

public class Payment {

    private int id;
    private int repairId;

    private String customerName;
    private String deviceModel;
    private String serviceName;
    private String branchName;

    private double amount;
    private String paymentDate;
    private String status;

    public Payment(
            int id,
            int repairId,
            String customerName,
            String deviceModel,
            String serviceName,
            String branchName,
            double amount,
            String paymentDate,
            String status
    ) {
        this.id = id;
        this.repairId = repairId;
        this.customerName = customerName;
        this.deviceModel = deviceModel;
        this.serviceName = serviceName;
        this.branchName = branchName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getRepairId() {
        return repairId;
    }

    public String getCustomerName() {
        return customerName;
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

    public double getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}