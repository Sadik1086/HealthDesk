package com.healthdesk.dao;

import com.healthdesk.database.DatabaseConnection;
import com.healthdesk.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private static final String SELECT_BASE =
            "SELECT a.*, p.name AS patient_name, d.name AS doctor_name " +
                    "FROM appointments a " +
                    "LEFT JOIN patients p ON a.patient_id = p.id " +
                    "LEFT JOIN doctors d ON a.doctor_id = d.id ";

    public List<Appointment> findAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY a.appointment_date DESC, a.time_slot";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading appointments: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Appointment> findToday() {
        List<Appointment> list = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE a.appointment_date = date('now') ORDER BY a.time_slot";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading today appointments: " + e.getMessage(), e);
        }
        return list;
    }

    public int countToday() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM appointments WHERE appointment_date = date('now')")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public int countPending() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM appointments WHERE status='SCHEDULED'")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public void insert(Appointment a) {
        String sql = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, time_slot, reason, status, notes) VALUES(?,?,?,?,?,?,?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setString(3, a.getAppointmentDate());
            ps.setString(4, a.getTimeSlot());
            ps.setString(5, a.getReason());
            ps.setString(6, a.getStatus());
            ps.setString(7, a.getNotes());
            ps.executeUpdate();
            a.setId(DatabaseConnection.lastInsertRowId(conn));
        } catch (SQLException e) {
            throw new RuntimeException("Error creating appointment: " + e.getMessage(), e);
        }
    }

    public void updateStatus(int id, String status) {
        String sql = "UPDATE appointments SET status=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment status: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM appointments WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    private Appointment map(ResultSet rs) throws SQLException {
        Appointment a = new Appointment();
        a.setId(rs.getInt("id"));
        a.setPatientId(rs.getInt("patient_id"));
        a.setPatientName(rs.getString("patient_name"));
        a.setDoctorId(rs.getInt("doctor_id"));
        a.setDoctorName(rs.getString("doctor_name"));
        a.setAppointmentDate(rs.getString("appointment_date"));
        a.setTimeSlot(rs.getString("time_slot"));
        a.setReason(rs.getString("reason"));
        a.setStatus(rs.getString("status"));
        a.setNotes(rs.getString("notes"));
        a.setCreatedAt(rs.getString("created_at"));
        return a;
    }
}
