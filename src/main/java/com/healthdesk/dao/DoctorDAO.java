package com.healthdesk.dao;

import com.healthdesk.database.DatabaseConnection;
import com.healthdesk.model.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    private static final String SELECT_BASE =
            "SELECT d.*, dept.name AS dept_name FROM doctors d " +
                    "LEFT JOIN departments dept ON d.department_id = dept.id ";

    public List<Doctor> findAll() {
        List<Doctor> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY d.name";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading doctors: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Doctor> findAvailable() {
        List<Doctor> list = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE d.available = 1 ORDER BY d.name";
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error loading available doctors: " + e.getMessage(), e);
        }
        return list;
    }

    public int countAvailable() {
        try (Statement st = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM doctors WHERE available=1")) {
            rs.next(); return rs.getInt(1);
        } catch (SQLException e) { return 0; }
    }

    public void insert(Doctor d) {
        String sql = "INSERT INTO doctors(name, specialization, phone, email, department_id, available) VALUES(?,?,?,?,?,?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getSpecialization());
            ps.setString(3, d.getPhone());
            ps.setString(4, d.getEmail());
            ps.setInt(5, d.getDepartmentId());
            ps.setInt(6, d.isAvailable() ? 1 : 0);
            ps.executeUpdate();
            d.setId(DatabaseConnection.lastInsertRowId(conn));
        } catch (SQLException e) {
            throw new RuntimeException("Error creating doctor: " + e.getMessage(), e);
        }
    }

    public void update(Doctor d) {
        String sql = "UPDATE doctors SET name=?, specialization=?, phone=?, email=?, department_id=?, available=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, d.getName());
            ps.setString(2, d.getSpecialization());
            ps.setString(3, d.getPhone());
            ps.setString(4, d.getEmail());
            ps.setInt(5, d.getDepartmentId());
            ps.setInt(6, d.isAvailable() ? 1 : 0);
            ps.setInt(7, d.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating doctor: " + e.getMessage(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM doctors WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting doctor: " + e.getMessage(), e);
        }
    }

    private Doctor map(ResultSet rs) throws SQLException {
        Doctor d = new Doctor();
        d.setId(rs.getInt("id"));
        d.setName(rs.getString("name"));
        d.setSpecialization(rs.getString("specialization"));
        d.setPhone(rs.getString("phone"));
        d.setEmail(rs.getString("email"));
        d.setDepartmentId(rs.getInt("department_id"));
        d.setDepartmentName(rs.getString("dept_name"));
        d.setAvailable(rs.getInt("available") == 1);
        return d;
    }
}
