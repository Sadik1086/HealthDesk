package com.healthdesk.controller;

import com.healthdesk.dao.BillDAO;
import com.healthdesk.dao.PatientDAO;
import com.healthdesk.model.Bill;
import com.healthdesk.model.Patient;
import com.healthdesk.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class BillingController {

    @FXML private TableView<Bill>           table;
    @FXML private TableColumn<Bill, Number>  colId;
    @FXML private TableColumn<Bill, String>  colPatient, colStatus, colMethod, colDate;
    @FXML private TableColumn<Bill, Number>  colTotal;

    @FXML private ComboBox<Patient> cbPatient;
    @FXML private TextField tfConsultation, tfLab, tfBed, tfMedicine;
    @FXML private Label     lblTotal;
    @FXML private ComboBox<String> cbPayMethod;

    private final BillDAO    billDAO    = new BillDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private Bill selected;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> c.getValue().idProperty());
        colPatient.setCellValueFactory(c -> c.getValue().patientNameProperty());
        colTotal.setCellValueFactory(c -> c.getValue().totalProperty());
        colStatus.setCellValueFactory(c -> c.getValue().paymentStatusProperty());
        colMethod.setCellValueFactory(c -> {
            String m = c.getValue().getPaymentMethod();
            return new javafx.beans.property.SimpleStringProperty(m != null ? m : "");
        });
        colDate.setCellValueFactory(c -> c.getValue().createdAtProperty());

        cbPatient.setItems(FXCollections.observableArrayList(patientDAO.findAll()));
        cbPatient.setConverter(new javafx.util.StringConverter<>() {
            public String toString(Patient p) { return p == null ? "" : p.getName(); }
            public Patient fromString(String s) { return null; }
        });

        cbPayMethod.setItems(FXCollections.observableArrayList("Cash", "Card", "Insurance", "Online"));

        // Auto-calculate total when any field changes
        javafx.beans.value.ChangeListener<String> recalc = (obs, o, n) -> recalcTotal();
        tfConsultation.textProperty().addListener(recalc);
        tfLab.textProperty().addListener(recalc);
        tfBed.textProperty().addListener(recalc);
        tfMedicine.textProperty().addListener(recalc);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> selected = val);

        loadAll();
    }

    private void recalcTotal() {
        double total = parseField(tfConsultation) + parseField(tfLab)
                + parseField(tfBed) + parseField(tfMedicine);
        lblTotal.setText(String.format("Total: ৳ %.2f", total));
    }

    private double parseField(TextField tf) {
        try { return Double.parseDouble(tf.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private void loadAll() {
        table.setItems(FXCollections.observableArrayList(billDAO.findAll()));
    }

    @FXML
    private void handleCreateBill() {
        if (cbPatient.getValue() == null) { AlertUtil.error("Validation", "Select a patient."); return; }

        double consultation = parseField(tfConsultation);
        double lab          = parseField(tfLab);
        double bed          = parseField(tfBed);
        double medicine     = parseField(tfMedicine);
        double total        = consultation + lab + bed + medicine;

        Bill b = new Bill();
        b.setPatientId(cbPatient.getValue().getId());
        b.setConsultationFee(consultation);
        b.setLabCharges(lab);
        b.setBedCharges(bed);
        b.setMedicineCharges(medicine);
        b.setTotal(total);
        b.setPaymentStatus("UNPAID");

        billDAO.insert(b);
        handleClear();
        loadAll();
        AlertUtil.info("Bill Created", "Bill created for " + cbPatient.getValue().getName() + ". Total: ৳ " + String.format("%.2f", total));
    }

    @FXML
    private void handleMarkPaid() {
        if (selected == null) { AlertUtil.error("No Selection", "Select a bill first."); return; }
        if ("PAID".equals(selected.getPaymentStatus())) { AlertUtil.info("Already Paid", "This bill is already paid."); return; }
        if (cbPayMethod.getValue() == null) { AlertUtil.error("Validation", "Select a payment method."); return; }

        billDAO.markPaid(selected.getId(), cbPayMethod.getValue());
        loadAll();
        AlertUtil.info("Paid", "Bill marked as paid via " + cbPayMethod.getValue());
    }

    @FXML
    private void handleClear() {
        selected = null;
        cbPatient.setValue(null);
        tfConsultation.clear(); tfLab.clear(); tfBed.clear(); tfMedicine.clear();
        lblTotal.setText("Total: ৳ 0.00");
        cbPayMethod.setValue(null);
        table.getSelectionModel().clearSelection();
    }
}
