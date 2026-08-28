package com.up9.techfix.admin;
public class Payment {

    private int id;
    private String paymentReference;
    private int appointmentId;
    private String customerName;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDate;

    public Payment(
            int id,
            String paymentReference,
            int appointmentId,
            String customerName,
            double amount,
            String paymentMethod,
            String paymentStatus,
            String paymentDate
    ) {
        this.id = id;
        this.paymentReference = paymentReference;
        this.appointmentId = appointmentId;
        this.customerName = customerName;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public int getId() {
        return id;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}