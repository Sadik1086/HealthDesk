package com.healthdesk.controller;

import com.healthdesk.dao.PatientDAO;
import com.healthdesk.model.Patient;
import com.healthdesk.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PatientController {

    @FXML private TableView<Patient>          table;
    @FXML private TableColumn<Patient, Number> colId;
    @FXML private TableColumn<Patient, String> colName, colGender, colPhone, colBlood, colDate;
    @FXML private TableColumn<Patient, Number> colAge;

    @FXML private TextField tfName, tfPhone, tfEmail, tfAddress, tfSearch;
    @FXML private TextField tfAge;
    @FXML private ComboBox<String> cbGender, cbBlood;

    private final PatientDAO dao = new PatientDAO();
    private Patient selected;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colAge.setCellValueFactory(c -> c.getValue().ageProperty());
        colGender.setCellValueFactory(c -> c.getValue().genderProperty());
        colPhone.setCellValueFactory(c -> c.getValue().phoneProperty());
        colBlood.setCellValueFactory(c -> c.getValue().bloodGroupProperty());
        colDate.setCellValueFactory(c -> c.getValue().registeredAtProperty());

        cbGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));
        cbBlood.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            selected = val;
            if (val != null) populate(val);
        });

        loadAll();
    }

    private void loadAll() {
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    private void populate(Patient p) {
        tfName.setText(p.getName());
        tfAge.setText(String.valueOf(p.getAge()));
        cbGender.setValue(p.getGender());
        tfPhone.setText(p.getPhone());
        tfEmail.setText(p.getEmail());
        tfAddress.setText(p.getAddress());
        cbBlood.setValue(p.getBloodGroup());
    }

    @FXML
    private void handleSave() {
        String name = tfName.getText() == null ? "" : tfName.getText().trim();
        if (name.isEmpty()) { AlertUtil.error("Validation", "Patient name is required."); return; }

        Patient p = selected != null ? selected : new Patient();
        p.setName(name);
        try { p.setAge(Integer.parseInt(tfAge.getText().trim())); } catch (NumberFormatException e) { p.setAge(0); }
        p.setGender(cbGender.getValue());
        p.setPhone(tfPhone.getText());
        p.setEmail(tfEmail.getText());
        p.setAddress(tfAddress.getText());
        p.setBloodGroup(cbBlood.getValue());

        if (p.getId() == 0) dao.insert(p);
        else dao.update(p);

        handleClear();
        loadAll();
        AlertUtil.info("Success", "Patient saved successfully.");
    }

    @FXML
    private void handleDelete() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a patient first."); return; }
        if (!AlertUtil.confirm("Confirm Delete", "Delete patient: " + selected.getName() + "?")) return;
        dao.delete(selected.getId());
        handleClear();
        loadAll();
    }

    @FXML
    private void handleClear() {
        selected = null;
        tfName.clear(); tfAge.clear(); tfPhone.clear(); tfEmail.clear(); tfAddress.clear();
        cbGender.setValue(null); cbBlood.setValue(null);
        table.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearch() {
        String kw = tfSearch.getText() == null ? "" : tfSearch.getText().trim();
        if (kw.isEmpty()) { loadAll(); return; }
        table.setItems(FXCollections.observableArrayList(dao.search(kw)));
    }
}
