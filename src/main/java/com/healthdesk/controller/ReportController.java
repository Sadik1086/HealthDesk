package com.healthdesk.controller;

import com.healthdesk.dao.*;
import com.healthdesk.database.DatabaseConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportController {

    @FXML private Label lblTotalPatients, lblTotalRevenue, lblPaidBills, lblUnpaidBills;
    @FXML private Label lblTodayAppointments, lblPendingAppointments;
    @FXML private Label lblOccupiedBeds, lblAvailableBeds;
    @FXML private Label lblPendingLab, lblCompletedLab;

    @FXML private BarChart<String, Number> deptChart;

    @FXML private TableView<String[]>               apptTable;
    @FXML private TableColumn<String[], String>     colApptPatient, colApptDoctor, colApptDate, colApptStatus;

    private final PatientDAO     patientDAO     = new PatientDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final BedDAO         bedDAO         = new BedDAO();
    private final BillDAO        billDAO        = new BillDAO();
    private final LabDAO         labDAO         = new LabDAO();

    @FXML
    public void initialize() {
        loadSummary();
        loadDeptChart();
        setupAppointmentTable();
        loadAppointmentReport();
    }

    private void loadSummary() {
        lblTotalPatients.setText(String.valueOf(patientDAO.countAll()));
        lblTotalRevenue.setText(String.format("৳ %.2f", billDAO.totalRevenue()));
        lblUnpaidBills.setText(String.valueOf(billDAO.countUnpaid()));
        lblOccupiedBeds.setText(String.valueOf(bedDAO.countOccupied()));
        lblAvailableBeds.setText(String.valueOf(bedDAO.countAvailable()));
        lblTodayAppointments.setText(String.valueOf(appointmentDAO.countToday()));
        lblPendingAppointments.setText(String.valueOf(appointmentDAO.countPending()));
        lblPendingLab.setText(String.valueOf(labDAO.countPendingOrders()));

        // Paid bills count
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM bills WHERE payment_status='PAID'")) {
            rs.next(); lblPaidBills.setText(rs.getString(1));
        } catch (Exception e) { lblPaidBills.setText("0"); }

        // Completed lab
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM lab_orders WHERE status='COMPLETED'")) {
            rs.next(); lblCompletedLab.setText(rs.getString(1));
        } catch (Exception e) { lblCompletedLab.setText("0"); }
    }

    private void loadDeptChart() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String sql = "SELECT dept.name, COUNT(d.id) AS cnt FROM doctors d " +
                "JOIN departments dept ON d.department_id = dept.id GROUP BY dept.name";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) data.put(rs.getString("name"), rs.getInt("cnt"));
        } catch (Exception e) { e.printStackTrace(); }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doctors per Department");
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        deptChart.getData().clear();
        deptChart.getData().add(series);
    }

    private void setupAppointmentTable() {
        colApptPatient.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[0]));
        colApptDoctor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[1]));
        colApptDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[2]));
        colApptStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[3]));
    }

    private void loadAppointmentReport() {
        var rows = FXCollections.<String[]>observableArrayList();
        String sql = "SELECT p.name, d.name, a.appointment_date, a.status " +
                "FROM appointments a JOIN patients p ON a.patient_id=p.id " +
                "JOIN doctors d ON a.doctor_id=d.id ORDER BY a.appointment_date DESC LIMIT 20";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getString(4)
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        apptTable.setItems(rows);
    }

    @FXML
    private void handleRefresh() {
        loadSummary();
        loadDeptChart();
        loadAppointmentReport();
    }
}
