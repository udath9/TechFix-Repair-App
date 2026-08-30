package com.up9.techfix.Technician;

public class Repair {

    private int id;
    private String deviceCategory;
    private String deviceModel;
    private String serviceName;
    private String problemDescription;
    private String status;
    private String repairDate;

    public Repair(int id,
                  String deviceCategory,
                  String deviceModel,
                  String serviceName,
                  String problemDescription,
                  String status,
                  String repairDate) {

        this.id = id;
        this.deviceCategory = deviceCategory;
        this.deviceModel = deviceModel;
        this.serviceName = serviceName;
        this.problemDescription = problemDescription;
        this.status = status;
        this.repairDate = repairDate;
    }

    public int getId() {
        return id;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getStatus() {
        return status;
    }

    public String getRepairDate() {
        return repairDate;
    }
}