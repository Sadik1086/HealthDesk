package com.healthdesk.controller;

import com.healthdesk.dao.DoctorDAO;
import com.healthdesk.dao.LabDAO;
import com.healthdesk.dao.PatientDAO;
import com.healthdesk.model.Doctor;
import com.healthdesk.model.LabOrder;
import com.healthdesk.model.LabTest;
import com.healthdesk.model.Patient;
import com.healthdesk.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class LabController {

    @FXML private TableView<LabOrder>           table;
    @FXML private TableColumn<LabOrder, Number>  colId;
    @FXML private TableColumn<LabOrder, String>  colPatient, colTest, colDoctor, colOrdered, colStatus;

    @FXML private ComboBox<Patient>  cbPatient;
    @FXML private ComboBox<LabTest>  cbTest;
    @FXML private ComboBox<Doctor>   cbDoctor;
    @FXML private TextField          tfResult;

    private final LabDAO     labDAO     = new LabDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final DoctorDAO  doctorDAO  = new DoctorDAO();
    private LabOrder selected;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colPatient.setCellValueFactory(c -> c.getValue().patientNameProperty());
        colTest.setCellValueFactory(c -> c.getValue().testNameProperty());
        colDoctor.setCellValueFactory(c -> c.getValue().doctorNameProperty());
        colOrdered.setCellValueFactory(c -> c.getValue().orderedAtProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        cbPatient.setItems(FXCollections.observableArrayList(patientDAO.findAll()));
        cbPatient.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getName(); }
            public Patient fromString(String s) { return null; }
        });

        cbTest.setItems(FXCollections.observableArrayList(labDAO.findAllTests()));
        cbTest.setConverter(new javafx.util.StringConverter<>() {
            public String toString(LabTest t) { return t == null ? "" : t.getName() + " (৳" + (int) t.getPrice() + ")"; }
            public LabTest fromString(String s) { return null; }
        });

        cbDoctor.setItems(FXCollections.observableArrayList(doctorDAO.findAll()));
        cbDoctor.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Doctor d) { return d == null ? "" : d.getName(); }
            public Doctor fromString(String s) { return null; }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> selected = val);

        loadAll();
    }

    private void loadAll() {
        table.setItems(FXCollections.observableArrayList(labDAO.findAllOrders()));
    }

    @FXML
    private void handleOrder() {
        if (cbPatient.getValue() == null) { AlertUtil.error("Validation", "Select a patient."); return; }
        if (cbTest.getValue() == null)    { AlertUtil.error("Validation", "Select a lab test."); return; }

        LabOrder o = new LabOrder();
        o.setPatientId(cbPatient.getValue().getId());
        o.setTestId(cbTest.getValue().getId());
        o.setDoctorId(cbDoctor.getValue() != null ? cbDoctor.getValue().getId() : 0);

        labDAO.insertOrder(o);
        cbPatient.setValue(null); cbTest.setValue(null); cbDoctor.setValue(null);
        loadAll();
        AlertUtil.info("Ordered", "Lab test ordered successfully.");
    }

    @FXML
    private void handleEnterResult() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a lab order first."); return; }
        String result = tfResult.getText() == null ? "" : tfResult.getText().trim();
        if (result.isEmpty()) { AlertUtil.error("Validation", "Enter the test result."); return; }

        labDAO.updateResult(selected.getId(), result);
        tfResult.clear();
        loadAll();
        AlertUtil.info("Result Saved", "Lab result saved successfully.");
    }
}
