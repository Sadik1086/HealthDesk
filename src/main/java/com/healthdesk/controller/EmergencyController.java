package com.healthdesk.controller;

import com.healthdesk.dao.EmergencyQueueDAO;
import com.healthdesk.dao.PatientDAO;
import com.healthdesk.model.EmergencyQueue;
import com.healthdesk.model.Patient;
import com.healthdesk.service.EmergencyMonitorService;
import com.healthdesk.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class EmergencyController {

    @FXML private TableView<EmergencyQueue>           table;
    @FXML private TableColumn<EmergencyQueue, Number>  colId;
    @FXML private TableColumn<EmergencyQueue, String>  colPatient, colPriority, colDesc, colStatus, colArrived;

    @FXML private ComboBox<Patient>  cbPatient;
    @FXML private ComboBox<String>   cbPriority;
    @FXML private TextField          tfDescription;
    @FXML private Label              lblQueueCount;

    private final EmergencyQueueDAO dao        = new EmergencyQueueDAO();
    private final PatientDAO        patientDAO = new PatientDAO();

    private final EmergencyMonitorService monitor = new EmergencyMonitorService();

    private EmergencyQueue selected;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colPatient.setCellValueFactory(c -> c.getValue().patientNameProperty());
        colPriority.setCellValueFactory(c -> c.getValue().priorityProperty());
        colDesc.setCellValueFactory(c -> c.getValue().descriptionProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());
        colArrived.setCellValueFactory(c -> c.getValue().arrivedAtProperty());

        List<Patient> patients = patientDAO.findAll();
        cbPatient.setItems(FXCollections.observableArrayList(patients));
        cbPatient.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getName(); }
            public Patient fromString(String s) { return null; }
        });

        cbPriority.setItems(FXCollections.observableArrayList(
                "1 - Critical", "2 - Urgent", "3 - Normal"));
        cbPriority.setValue("3 - Normal");

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> selected = val);

        // Start live background monitoring - updates every 4 seconds
        monitor.startMonitoring(4, queue -> {
            table.setItems(FXCollections.observableArrayList(queue));
            lblQueueCount.setText("Waiting: " + queue.size());
        });
    }

    @FXML
    private void handleAdd() {
        if (cbPatient.getValue() == null) { AlertUtil.error("Validation", "Select a patient."); return; }

        EmergencyQueue eq = new EmergencyQueue();
        eq.setPatientId(cbPatient.getValue().getId());
        eq.setPriority(cbPriority.getSelectionModel().getSelectedIndex() + 1);
        eq.setDescription(tfDescription.getText());
        eq.setStatus("WAITING");

        dao.insert(eq);
        cbPatient.setValue(null);
        tfDescription.clear();
        cbPriority.setValue("3 - Normal");
        AlertUtil.info("Added", "Patient added to emergency queue.");
    }

    @FXML
    private void handleAttend() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a patient from the queue."); return; }
        dao.markAttended(selected.getId());
        AlertUtil.info("Attended", selected.getPatientName() + " has been attended.");
    }

    @FXML
    private void handleRemove() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a patient to remove."); return; }
        if (!AlertUtil.confirm("Remove", "Remove " + selected.getPatientName() + " from queue?")) return;
        dao.delete(selected.getId());
    }
}
