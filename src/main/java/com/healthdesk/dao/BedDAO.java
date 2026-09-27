package com.healthdesk.dao;

import com.healthdesk.database.DatabaseConnection;
import com.healthdesk.model.Bed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BedDAO {

    private static final String SELECT_BASE =
            "SELECT b.*, p.name AS patient_name FROM beds b " +
                    "LEFT JOIN patients p ON b.patient_id = p.id ";

    public List<Bed> findAll() {
        List<Bed> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY b.ward, b.bed_number";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading beds: " + e.getMessage(), e);
        }
        return list;
    }

    public int countOccupied() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM beds WHERE status='OCCUPIED'")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public int countAvailable() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM beds WHERE status='AVAILABLE'")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public void admitPatient(int bedId, int patientId) {
        String sql = "UPDATE beds SET patient_id=?, status='OCCUPIED', admitted_at=datetime('now') WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, bedId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error admitting patient to bed: " + e.getMessage(), e);
        }
    }

    public void dischargeBed(int bedId) {
        String sql = "UPDATE beds SET patient_id=NULL, status='AVAILABLE', admitted_at=NULL WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bedId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error discharging bed: " + e.getMessage(), e);
        }
    }

    private Bed map(ResultSet rs) throws SQLException {
        Bed b = new Bed();
        b.setId(rs.getInt("id"));
        b.setBedNumber(rs.getString("bed_number"));
        b.setWard(rs.getString("ward"));
        b.setPatientId(rs.getInt("patient_id"));
        b.setPatientName(rs.getString("patient_name"));
        b.setStatus(rs.getString("status"));
        b.setAdmittedAt(rs.getString("admitted_at"));
        return b;
    }
}
