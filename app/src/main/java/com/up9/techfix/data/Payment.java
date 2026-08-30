package com.up9.techfix.data;

public class Payment {

    private int id;
    private int repairId;
    private double amount;
    private String paymentDate;
    private String status;

    public Payment(
            int id,
            int repairId,
            double amount,
            String paymentDate,
            String status
    ) {
        this.id = id;
        this.repairId = repairId;
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

    public double getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getStatus() {
        return status;
    }
}