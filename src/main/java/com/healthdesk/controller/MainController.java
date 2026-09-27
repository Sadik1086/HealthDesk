package com.healthdesk.controller;

import com.healthdesk.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    @FXML private StackPane contentArea;
    @FXML private Label     pageTitleLabel;
    @FXML private Label     userBadge;
    @FXML private Label     emergencyBadge;

    @FXML private Button navDashboard, navPatients, navDoctors, navAppointments,
            navEmergency, navBeds, navLab, navBilling, navDrugInfo, navReports;

    private final Map<String, Parent> pageCache = new HashMap<>();
    private Button activeNav;

    @FXML
    public void initialize() {
        userBadge.setText(SessionManager.getCurrentUser().getFullName()
                + " (" + SessionManager.getCurrentUser().getRole() + ")");
        showDashboard();
    }

    private void navigate(String fxml, String title, Button nav) {
        try {
            if (!pageCache.containsKey(fxml)) {
                Parent page = FXMLLoader.load(getClass().getResource("/fxml/" + fxml));
                pageCache.put(fxml, page);
            }
            contentArea.getChildren().setAll(pageCache.get(fxml));
            pageTitleLabel.setText(title);
            if (activeNav != null) activeNav.getStyleClass().remove("nav-button-active");
            nav.getStyleClass().add("nav-button-active");
            activeNav = nav;
        } catch (IOException e) {
            throw new RuntimeException("Could not load: " + fxml, e);
        }
    }

    public void updateEmergencyBadge(int count) {
        if (count > 0) {
            emergencyBadge.setText(count + " waiting");
            emergencyBadge.setVisible(true);
            emergencyBadge.setManaged(true);
        } else {
            emergencyBadge.setVisible(false);
            emergencyBadge.setManaged(false);
        }
    }

    @FXML public void showDashboard()    { navigate("dashboard.fxml",    "Dashboard",        navDashboard); }
    @FXML public void showPatients()     { navigate("patients.fxml",     "Patients",         navPatients); }
    @FXML public void showDoctors()      { navigate("doctors.fxml",      "Doctors",          navDoctors); }
    @FXML public void showAppointments() { navigate("appointments.fxml", "Appointments",     navAppointments); }
    @FXML public void showEmergency()    { navigate("emergency.fxml",    "Emergency Queue",  navEmergency); }
    @FXML public void showBeds()         { navigate("beds.fxml",         "Bed Management",   navBeds); }
    @FXML public void showLab()          { navigate("lab.fxml",          "Lab Services",     navLab); }
    @FXML public void showBilling()      { navigate("billing.fxml",      "Billing",          navBilling); }
    @FXML public void showDrugInfo()     { navigate("druginfo.fxml",     "Drug Info",        navDrugInfo); }
    @FXML public void showReports()      { navigate("reports.fxml",      "Reports",          navReports); }

    @FXML
    public void handleLogout() {
        SessionManager.logout();
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            contentArea.getScene().setRoot(login);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
