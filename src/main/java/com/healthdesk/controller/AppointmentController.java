package com.healthdesk.controller;

import com.healthdesk.dao.AppointmentDAO;
import com.healthdesk.dao.DoctorDAO;
import com.healthdesk.dao.PatientDAO;
import com.healthdesk.model.Appointment;
import com.healthdesk.model.Doctor;
import com.healthdesk.model.Patient;
import com.healthdesk.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class AppointmentController {

    @FXML private TableView<Appointment>           table;
    @FXML private TableColumn<Appointment, Number>  colId;
    @FXML private TableColumn<Appointment, String>  colPatient, colDoctor, colDate, colTime, colReason, colStatus;

    @FXML private ComboBox<Patient> cbPatient;
    @FXML private ComboBox<Doctor>  cbDoctor;
    @FXML private DatePicker        dpDate;
    @FXML private ComboBox<String>  cbTimeSlot, cbStatus;
    @FXML private TextField         tfReason;

    private final AppointmentDAO dao        = new AppointmentDAO();
    private final PatientDAO     patientDAO = new PatientDAO();
    private final DoctorDAO      doctorDAO  = new DoctorDAO();
    private Appointment selected;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colPatient.setCellValueFactory(c -> c.getValue().patientNameProperty());
        colDoctor.setCellValueFactory(c -> c.getValue().doctorNameProperty());
        colDate.setCellValueFactory(c -> c.getValue().appointmentDateProperty());
        colTime.setCellValueFactory(c -> c.getValue().timeSlotProperty());
        colReason.setCellValueFactory(c -> c.getValue().reasonProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        List<Patient> patients = patientDAO.findAll();
        cbPatient.setItems(FXCollections.observableArrayList(patients));
        cbPatient.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getName(); }
            public Patient fromString(String s) { return null; }
        });

        List<Doctor> doctors = doctorDAO.findAvailable();
        cbDoctor.setItems(FXCollections.observableArrayList(doctors));
        cbDoctor.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Doctor d) { return d == null ? "" : d.getName(); }
            public Doctor fromString(String s) { return null; }
        });

        cbTimeSlot.setItems(FXCollections.observableArrayList(
                "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM",
                "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM",
                "04:00 PM", "05:00 PM"));

        cbStatus.setItems(FXCollections.observableArrayList("SCHEDULED", "COMPLETED", "CANCELLED"));

        dpDate.setValue(LocalDate.now());

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            selected = val;
            if (val != null) populate(val);
        });

        loadAll();
    }

    private void loadAll() {
        table.setItems(FXCollections.observableArrayList(dao.findAll()));
    }

    private void populate(Appointment a) {
        cbPatient.getItems().stream()
                .filter(p -> p.getId() == a.getPatientId())
                .findFirst().ifPresent(cbPatient::setValue);
        cbDoctor.getItems().stream()
                .filter(d -> d.getId() == a.getDoctorId())
                .findFirst().ifPresent(cbDoctor::setValue);
        dpDate.setValue(LocalDate.parse(a.getAppointmentDate()));
        cbTimeSlot.setValue(a.getTimeSlot());
        tfReason.setText(a.getReason());
        cbStatus.setValue(a.getStatus());
    }

    @FXML
    private void handleSave() {
        if (cbPatient.getValue() == null) { AlertUtil.error("Validation", "Select a patient."); return; }
        if (cbDoctor.getValue() == null)  { AlertUtil.error("Validation", "Select a doctor."); return; }
        if (dpDate.getValue() == null)    { AlertUtil.error("Validation", "Select a date."); return; }
        if (cbTimeSlot.getValue() == null) { AlertUtil.error("Validation", "Select a time slot."); return; }

        Appointment a = new Appointment();
        a.setPatientId(cbPatient.getValue().getId());
        a.setDoctorId(cbDoctor.getValue().getId());
        a.setAppointmentDate(dpDate.getValue().toString());
        a.setTimeSlot(cbTimeSlot.getValue());
        a.setReason(tfReason.getText());
        a.setStatus(cbStatus.getValue() != null ? cbStatus.getValue() : "SCHEDULED");

        dao.insert(a);
        handleClear();
        loadAll();
        AlertUtil.info("Success", "Appointment booked successfully.");
    }

    @FXML
    private void handleMarkComplete() {
        if (selected == null) { AlertUtil.error("No Selection", "Select an appointment first."); return; }
        dao.updateStatus(selected.getId(), "COMPLETED");
        loadAll();
    }

    @FXML
    private void handleCancel() {
        if (selected == null) { AlertUtil.error("No Selection", "Select an appointment first."); return; }
        if (!AlertUtil.confirm("Cancel Appointment", "Cancel appointment for " + selected.getPatientName() + "?")) return;
        dao.updateStatus(selected.getId(), "CANCELLED");
        loadAll();
    }

    @FXML
    private void handleClear() {
        selected = null;
        cbPatient.setValue(null); cbDoctor.setValue(null);
        dpDate.setValue(LocalDate.now());
        cbTimeSlot.setValue(null); tfReason.clear(); cbStatus.setValue(null);
        table.getSelectionModel().clearSelection();
    }
}
