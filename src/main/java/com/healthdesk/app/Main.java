package com.healthdesk.app;

import com.healthdesk.models.Patient;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== HealthDesk System Test ===");

        Patient p1 = new Patient(101,  45,"Rahim Ahmed", "Fever", "Admitted");
        Patient p2 = new Patient(102,  30, "Karim Chowdhury","Dental Care", "In Treatment");

        p1.displayInfo();
        p2.displayInfo();
    }
}