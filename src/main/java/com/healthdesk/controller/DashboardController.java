package com.healthdesk.controller;

import com.healthdesk.dao.*;
import com.healthdesk.service.EmergencyMonitorService;
import com.healthdesk.service.HealthApiService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

public class DashboardController {

    @FXML private Label lblTotalPatients, lblTodayAppointments, lblAvailableDoctors;
    @FXML private Label lblOccupiedBeds, lblEmergencyWaiting, lblPendingLab;
    @FXML private Label lblTotalRevenue, lblUnpaidBills;
    @FXML private Label tipLabel;
    @FXML private ProgressIndicator tipProgress;

    private final PatientDAO     patientDAO     = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final DoctorDAO      doctorDAO      = new DoctorDAO();
    private final BedDAO         bedDAO         = new BedDAO();
    private final EmergencyQueueDAO emergencyDAO = new EmergencyQueueDAO();
    private final LabDAO         labDAO         = new LabDAO();
    private final BillDAO        billDAO        = new BillDAO();
    private final HealthApiService apiService   = new HealthApiService();

    private final EmergencyMonitorService monitor = new EmergencyMonitorService();

    @FXML
    public void initialize() {
        loadStats();
        startEmergencyMonitor();
    }

    private void loadStats() {
        lblTotalPatients.setText(String.valueOf(patientDAO.countAll()));
        lblTodayAppointments.setText(String.valueOf(appointmentDAO.countToday()));
        lblAvailableDoctors.setText(String.valueOf(doctorDAO.countAvailable()));
        lblOccupiedBeds.setText(String.valueOf(bedDAO.countOccupied()));
        lblEmergencyWaiting.setText(String.valueOf(emergencyDAO.countWaiting()));
        lblPendingLab.setText(String.valueOf(labDAO.countPendingOrders()));
        lblTotalRevenue.setText(String.format("৳ %.2f", billDAO.totalRevenue()));
        lblUnpaidBills.setText(String.valueOf(billDAO.countUnpaid()));
    }

    private void startEmergencyMonitor() {
        monitor.startMonitoring(5, queue -> {
            // This runs on the JavaFX Application Thread (via Platform.runLater inside the service)
            lblEmergencyWaiting.setText(String.valueOf(queue.size()));
        });
    }


    @FXML
    private void handleFetchTip() {
        tipProgress.setVisible(true);
        tipProgress.setManaged(true);
        tipLabel.setText("Fetching health tip from API...");

        Task<HealthApiService.HealthTip> task = new Task<>() {
            @Override
            protected HealthApiService.HealthTip call() {
                return apiService.fetchTip(); // runs off the UI thread
            }
        };

        task.setOnSucceeded(e -> {
            tipProgress.setVisible(false);
            tipProgress.setManaged(false);
            HealthApiService.HealthTip tip = task.getValue();
            if (tip == null) {
                tipLabel.setText("Could not reach the API. Check your internet connection.");
            } else {
                tipLabel.setText("💡 Tip #" + tip.id + ": " + tip.tip);
            }
        });

        task.setOnFailed(e -> {
            tipProgress.setVisible(false);
            tipProgress.setManaged(false);
            tipLabel.setText("Error fetching tip.");
        });

        Thread bgThread = new Thread(task, "healthdesk-api-fetch");
        bgThread.setDaemon(true);
        bgThread.start();
    }

    @FXML
    private void handleRefresh() {
        loadStats();
    }
}
