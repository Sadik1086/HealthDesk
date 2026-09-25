package com.healthdesk.service;

import com.healthdesk.models.Patient;
import java.util.ArrayList;
import java.util.List;

public class PatientService {

    private final List<Patient> patientList = new ArrayList<>();

    public boolean addPatient(Patient patient) {
        if (searchPatientById(patient.getId()) != null) {
            System.out.println("[ERROR] Patient Registration Failed: ID " + patient.getId() + " is already taken.");
            return false;
        }

        patientList.add(patient);
        System.out.println("[SUCCESS] Patient registered successfully in system memory.");
        return true;
    }

    public void viewAllPatients() {
        if (patientList.isEmpty()) {
            System.out.println("[INFO] No patient records available at the moment.");
            return;
        }

        System.out.println("\n===========================================================================================");
        System.out.println("                                 REGISTERED PATIENTS LIST                                  ");
        System.out.println("===========================================================================================");

        for (Patient patient : patientList) {
            patient.displayInfo();
        }

        System.out.println("===========================================================================================");
        System.out.println("Total Patients Registered: " + patientList.size());
    }

    public Patient searchPatientById(int id) {
        for (Patient patient : patientList) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public int getTotalPatientsCount() {
        return patientList.size();
    }
}