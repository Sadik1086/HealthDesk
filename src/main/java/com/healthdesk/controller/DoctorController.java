package com.healthdesk.controller;

import com.healthdesk.dao.DoctorDAO;
import com.healthdesk.database.DatabaseConnection;
import com.healthdesk.model.Doctor;
import com.healthdesk.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoctorController {

    @FXML private TableView<Doctor>           table;
    @FXML private TableColumn<Doctor, Number>  colId;
    @FXML private TableColumn<Doctor, String>  colName, colSpec, colDept, colPhone, colStatus;

    @FXML private TextField     tfName, tfSpec, tfPhone, tfEmail;
    @FXML private ComboBox<String> cbDept;
    @FXML private CheckBox      chkAvailable;

    private final DoctorDAO dao = new DoctorDAO();
    private Doctor selected;
    private List<Integer> deptIds = new ArrayList<>();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colName.setCellValueFactory(c -> c.getValue().nameProperty());
        colSpec.setCellValueFactory(c -> c.getValue().specializationProperty());
        colDept.setCellValueFactory(c -> c.getValue().departmentNameProperty());
        colPhone.setCellValueFactory(c -> c.getValue().phoneProperty());
        colStatus.setCellValueFactory(c -> c.getValue().availableProperty());
        loadDepartments();

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            selected = val;
            if (val != null) populate(val);
        });

        loadAll();
    }

    private void loadDepartments() {
        List<String> names = new ArrayList<>();
        deptIds.clear();
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name FROM departments ORDER BY name")) {
            while (rs.next()) {
                deptIds.add(rs.getInt("id"));
                names.add(rs.getString("name"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        cbDept.setItems(FXCollections.observableArrayList(names));
    }

    private void loadAll() {
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    private void populate(Doctor d) {
        tfName.setText(d.getName());
        tfSpec.setText(d.getSpecialization());
        tfPhone.setText(d.getPhone());
        tfEmail.setText(d.getEmail());
        chkAvailable.setSelected(d.isAvailable());
        // Find and set department in combo
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT name FROM departments WHERE id=" + d.getDepartmentId())) {
            if (rs.next()) cbDept.setValue(rs.getString("name"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    private void handleSave() {
        String name = tfName.getText() == null ? "" : tfName.getText().trim();
        if (name.isEmpty()) { AlertUtil.error("Validation", "Doctor name is required."); return; }
        int deptIdx = cbDept.getSelectionModel().getSelectedIndex();
        if (deptIdx < 0) { AlertUtil.error("Validation", "Select a department."); return; }

        Doctor d = selected != null ? selected : new Doctor();
        d.setName(name);
        d.setSpecialization(tfSpec.getText());
        d.setPhone(tfPhone.getText());
        d.setEmail(tfEmail.getText());
        d.setDepartmentId(deptIds.get(deptIdx));
        d.setAvailable(chkAvailable.isSelected());

        if (d.getId() == 0) dao.insert(d);
        else dao.update(d);

        handleClear();
        loadAll();
        AlertUtil.info("Success", "Doctor saved successfully.");
    }

    @FXML
    private void handleDelete() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a doctor first."); return; }
        if (!AlertUtil.confirm("Confirm Delete", "Delete doctor: " + selected.getName() + "?")) return;
        dao.delete(selected.getId());
        handleClear();
        loadAll();
    }

    @FXML
    private void handleClear() {
        selected = null;
        tfName.clear(); tfSpec.clear(); tfPhone.clear(); tfEmail.clear();
        cbDept.setValue(null); chkAvailable.setSelected(true);
        table.getSelectionModel().clearSelection();
    }
}
