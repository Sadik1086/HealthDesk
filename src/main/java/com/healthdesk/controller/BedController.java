package com.healthdesk.controller;

import com.healthdesk.dao.BedDAO;
import com.healthdesk.dao.PatientDAO;
import com.healthdesk.model.Bed;
import com.healthdesk.model.Patient;
import com.healthdesk.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class BedController {

    @FXML private TableView<Bed>           table;
    @FXML private TableColumn<Bed, Number>  colId;
    @FXML private TableColumn<Bed, String>  colBed, colWard, colPatient, colStatus, colAdmitted;

    @FXML private ComboBox<Patient> cbPatient;
    @FXML private Label lblAvailable, lblOccupied;

    private final BedDAO     bedDAO     = new BedDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private Bed selected;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colBed.setCellValueFactory(c -> c.getValue().bedNumberProperty());
        colWard.setCellValueFactory(c -> c.getValue().wardProperty());
        colPatient.setCellValueFactory(c -> c.getValue().patientNameProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());
        colAdmitted.setCellValueFactory(c -> c.getValue().admittedAtProperty());

        List<Patient> patients = patientDAO.findAll();
        cbPatient.setItems(FXCollections.observableArrayList(patients));
        cbPatient.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getName(); }
            public Patient fromString(String s) { return null; }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> selected = val);

        loadAll();
    }

    private void loadAll() {
        table.setItems(FXCollections.observableArrayList(bedDAO.findAll()));
        lblAvailable.setText("Available: " + bedDAO.countAvailable());
        lblOccupied.setText("Occupied: " + bedDAO.countOccupied());
    }

    @FXML
    private void handleAdmit() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a bed first."); return; }
        if (!"AVAILABLE".equals(selected.getStatus())) { AlertUtil.error("Bed Occupied", "This bed is already occupied."); return; }
        if (cbPatient.getValue() == null) { AlertUtil.error("No Patient", "Select a patient to admit."); return; }

        bedDAO.admitPatient(selected.getId(), cbPatient.getValue().getId());
        cbPatient.setValue(null);
        loadAll();
        AlertUtil.info("Admitted", "Patient admitted to bed " + selected.getBedNumber());
    }

    @FXML
    private void handleDischarge() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a bed first."); return; }
        if (!"OCCUPIED".equals(selected.getStatus())) { AlertUtil.error("Bed Empty", "This bed is not occupied."); return; }
        if (!AlertUtil.confirm("Discharge", "Discharge patient from bed " + selected.getBedNumber() + "?")) return;

        bedDAO.dischargeBed(selected.getId());
        loadAll();
        AlertUtil.info("Discharged", "Patient discharged from bed " + selected.getBedNumber());
    }

    @FXML
    private void handleRefresh() {
        loadAll();
    }
}
