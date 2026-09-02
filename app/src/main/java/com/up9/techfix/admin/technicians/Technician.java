package com.up9.techfix.admin.technicians;

public class Technician {

    private int id;
    private String name;
    private String phone;
    private String email;
    private String specialization;
    private String branch;
    private boolean available;

    public Technician(
            int id,
            String name,
            String phone,
            String email,
            String specialization,
            String branch,
            boolean available
    ) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
        this.branch = branch;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getBranch() {
        return branch;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}