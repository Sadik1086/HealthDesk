package com.healthdesk.dao;

import com.healthdesk.database.DatabaseConnection;
import com.healthdesk.model.EmergencyQueue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmergencyQueueDAO {

    private static final String SELECT_BASE =
            "SELECT eq.*, p.name AS patient_name FROM emergency_queue eq " +
                    "LEFT JOIN patients p ON eq.patient_id = p.id ";

    public List<EmergencyQueue> findWaiting() {
        List<EmergencyQueue> list = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE eq.status='WAITING' ORDER BY eq.priority ASC, eq.arrived_at ASC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading emergency queue: " + e.getMessage(), e);
        }
        return list;
    }

    public List<EmergencyQueue> findAll() {
        List<EmergencyQueue> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY eq.arrived_at DESC";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading emergency queue: " + e.getMessage(), e);
        }
        return list;
    }

    public int countWaiting() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM emergency_queue WHERE status='WAITING'")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public void insert(EmergencyQueue eq) {
        String sql = "INSERT INTO emergency_queue(patient_id, priority, description, status) VALUES(?,?,?,?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eq.getPatientId());
            ps.setInt(2, eq.getPriority());
            ps.setString(3, eq.getDescription());
            ps.setString(4, eq.getStatus());
            ps.executeUpdate();
            eq.setId(DatabaseConnection.lastInsertRowId(conn));
        } catch (SQLException e) {
            throw new RuntimeException("Error adding to emergency queue: " + e.getMessage(), e);
        }
    }

    public void markAttended(int id) {
        String sql = "UPDATE emergency_queue SET status='ATTENDED', attended_at=datetime('now') WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating emergency queue: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM emergency_queue WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing from queue: " + e.getMessage(), e);
        }
    }

    private EmergencyQueue map(ResultSet rs) throws SQLException {
        EmergencyQueue eq = new EmergencyQueue();
        eq.setId(rs.getInt("id"));
        eq.setPatientId(rs.getInt("patient_id"));
        eq.setPatientName(rs.getString("patient_name"));
        eq.setPriority(rs.getInt("priority"));
        eq.setDescription(rs.getString("description"));
        eq.setStatus(rs.getString("status"));
        eq.setArrivedAt(rs.getString("arrived_at"));
        eq.setAttendedAt(rs.getString("attended_at"));
        return eq;
    }
}
