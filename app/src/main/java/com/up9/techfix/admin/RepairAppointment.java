package com.up9.techfix.admin;

public class RepairAppointment {

    private int id;
    private String customerName;
    private String device;
    private String service;
    private String branch;
    private String technician;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private double estimatedPrice;

    public RepairAppointment(
            int id,
            String customerName,
            String device,
            String service,
            String branch,
            String technician,
            String appointmentDate,
            String appointmentTime,
            String status,
            double estimatedPrice
    ) {
        this.id = id;
        this.customerName = customerName;
        this.device = device;
        this.service = service;
        this.branch = branch;
        this.technician = technician;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.estimatedPrice = estimatedPrice;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDevice() {
        return device;
    }

    public String getService() {
        return service;
    }

    public String getBranch() {
        return branch;
    }

    public String getTechnician() {
        return technician;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEstimatedPrice(double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }
}