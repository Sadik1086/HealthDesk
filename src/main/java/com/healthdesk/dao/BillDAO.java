package com.healthdesk.dao;

import com.healthdesk.database.DatabaseConnection;
import com.healthdesk.model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    private static final String SELECT_BASE =
            "SELECT b.*, p.name AS patient_name FROM bills b " +
                    "LEFT JOIN patients p ON b.patient_id = p.id ";

    public List<Bill> findAll() {
        List<Bill> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY b.created_at DESC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading bills: " + e.getMessage(), e);
        }
        return list;
    }

    public double totalRevenue() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COALESCE(SUM(total),0) FROM bills WHERE payment_status='PAID'")) {
            rs.next(); return rs.getDouble(1);
        } catch (SQLException e) { return 0; }
    }

    public int countUnpaid() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM bills WHERE payment_status='UNPAID'")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public void insert(Bill b) {
        String sql = "INSERT INTO bills(patient_id, consultation_fee, lab_charges, bed_charges, medicine_charges, total, payment_status, payment_method) VALUES(?,?,?,?,?,?,?,?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getPatientId());
            ps.setDouble(2, b.getConsultationFee());
            ps.setDouble(3, b.getLabCharges());
            ps.setDouble(4, b.getBedCharges());
            ps.setDouble(5, b.getMedicineCharges());
            ps.setDouble(6, b.getTotal());
            ps.setString(7, b.getPaymentStatus());
            ps.setString(8, b.getPaymentMethod());
            ps.executeUpdate();
            b.setId(DatabaseConnection.lastInsertRowId(conn));
        } catch (SQLException e) {
            throw new RuntimeException("Error creating bill: " + e.getMessage(), e);
        }
    }

    public void markPaid(int id, String method) {
        String sql = "UPDATE bills SET payment_status='PAID', payment_method=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, method);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error marking bill as paid: " + e.getMessage(), e);
        }
    }

    private Bill map(ResultSet rs) throws SQLException {
        Bill b = new Bill();
        b.setId(rs.getInt("id"));
        b.setPatientId(rs.getInt("patient_id"));
        b.setPatientName(rs.getString("patient_name"));
        b.setConsultationFee(rs.getDouble("consultation_fee"));
        b.setLabCharges(rs.getDouble("lab_charges"));
        b.setBedCharges(rs.getDouble("bed_charges"));
        b.setMedicineCharges(rs.getDouble("medicine_charges"));
        b.setTotal(rs.getDouble("total"));
        b.setPaymentStatus(rs.getString("payment_status"));
        b.setPaymentMethod(rs.getString("payment_method"));
        b.setCreatedAt(rs.getString("created_at"));
        return b;
    }
}
