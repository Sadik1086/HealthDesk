package com.healthdesk.dao;

import com.healthdesk.database.DatabaseConnection;
import com.healthdesk.model.LabOrder;
import com.healthdesk.model.LabTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabDAO {

    public List<LabTest> findAllTests() {
        List<LabTest> list = new ArrayList<>();
        String sql = "SELECT * FROM lab_tests ORDER BY name";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                LabTest t = new LabTest();
                t.setId(rs.getInt("id"));
                t.setName(rs.getString("name"));
                t.setDescription(rs.getString("description"));
                t.setPrice(rs.getDouble("price"));
                list.add(t);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error loading lab tests: " + e.getMessage(), e);
        }
        return list;
    }

    public List<LabOrder> findAllOrders() {
        List<LabOrder> list = new ArrayList<>();
        String sql = "SELECT lo.*, p.name AS patient_name, lt.name AS test_name, d.name AS doctor_name " +
                "FROM lab_orders lo " +
                "LEFT JOIN patients p ON lo.patient_id = p.id " +
                "LEFT JOIN lab_tests lt ON lo.test_id = lt.id " +
                "LEFT JOIN doctors d ON lo.doctor_id = d.id " +
                "ORDER BY lo.ordered_at DESC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapOrder(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading lab orders: " + e.getMessage(), e);
        }
        return list;
    }

    public int countPendingOrders() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM lab_orders WHERE status='PENDING'")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public void insertOrder(LabOrder o) {
        String sql = "INSERT INTO lab_orders(patient_id, test_id, doctor_id, status) VALUES(?,?,?,?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, o.getPatientId());
            ps.setInt(2, o.getTestId());
            ps.setInt(3, o.getDoctorId());
            ps.setString(4, "PENDING");
            ps.executeUpdate();
            o.setId(DatabaseConnection.lastInsertRowId(conn));
        } catch (SQLException e) {
            throw new RuntimeException("Error creating lab order: " + e.getMessage(), e);
        }
    }

    public void updateResult(int orderId, String result) {
        String sql = "UPDATE lab_orders SET result=?, result_date=datetime('now'), status='COMPLETED' WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, result);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating lab result: " + e.getMessage(), e);
        }
    }

    private LabOrder mapOrder(ResultSet rs) throws SQLException {
        LabOrder o = new LabOrder();
        o.setId(rs.getInt("id"));
        o.setPatientId(rs.getInt("patient_id"));
        o.setPatientName(rs.getString("patient_name"));
        o.setTestId(rs.getInt("test_id"));
        o.setTestName(rs.getString("test_name"));
        o.setDoctorId(rs.getInt("doctor_id"));
        o.setDoctorName(rs.getString("doctor_name"));
        o.setOrderedAt(rs.getString("ordered_at"));
        o.setResult(rs.getString("result"));
        o.setResultDate(rs.getString("result_date"));
        o.setStatus(rs.getString("status"));
        return o;
    }
}
