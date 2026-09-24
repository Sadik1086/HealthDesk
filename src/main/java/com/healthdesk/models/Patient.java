package com.healthdesk.models;

public class Patient extends Person {
    private String disease;
    private String status;

    public Patient(int id, int age, String name, String disease, String status) {
        super(id, age, name);
        this.disease = disease;
        this.status = status;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public void displayInfo() {
        System.out.println("Patient ID: " + getId() +
                " | Name: " + getName() +
                " | Age: " + getAge() +
                " | Disease: " + disease +
                " | Status: " + status);
    }
}
