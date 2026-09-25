package com.healthdesk.models;

public class Doctor extends Person {
    private String specialization;
    private double visitingFee;
    private boolean isAvailable;

    public Doctor(int id, int age, String name, String specialization, double visitingFee, boolean isAvailable) {
        super(id, age, name);
        this.specialization = specialization;
        this.visitingFee = visitingFee;
        this.isAvailable = isAvailable;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getVisitingFee() {
        return visitingFee;
    }

    public void setVisitingFee(double visitingFee) {
        this.visitingFee = visitingFee;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public void displayInfo() {
        System.out.printf("ID: %-5d | Name: %-20s | Spec: %-18s | Fee: $%-7.2f | Status: %s%n",
                getId(), getName(), specialization, visitingFee, (isAvailable ? "Available" : "Busy"));
    }
}
