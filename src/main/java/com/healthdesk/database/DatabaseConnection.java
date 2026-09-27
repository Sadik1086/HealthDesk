package com.healthdesk.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseConnection {

    private static final String DB_FOLDER = "data";
    private static final String DB_FILE   = DB_FOLDER + "/healthdesk.db";
    private static final String DB_URL    = "jdbc:sqlite:" + DB_FILE;

    private static Connection connection;

    private DatabaseConnection() {}

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Files.createDirectories(Paths.get(DB_FOLDER));
                boolean freshDatabase = !Files.exists(Paths.get(DB_FILE));

                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(DB_URL);

                try (Statement st = connection.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON;");
                }

                if (freshDatabase) {
                    initializeSchema();
                }
            }
        } catch (ClassNotFoundException | SQLException | IOException e) {
            throw new RuntimeException("Failed to connect to HealthDesk database: " + e.getMessage(), e);
        }
        return connection;
    }

    private static void initializeSchema() {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/database/schema.sql")) {
            if (in == null) throw new RuntimeException("schema.sql not found on classpath");
            String sql = new String(in.readAllBytes());
            try (Statement st = connection.createStatement()) {
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        st.execute(trimmed);
                    }
                }
            }
            System.out.println("[HealthDesk] New database created at " + Paths.get(DB_FILE).toAbsolutePath());
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to initialize HealthDesk schema: " + e.getMessage(), e);
        }
    }

    public static int lastInsertRowId(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}
