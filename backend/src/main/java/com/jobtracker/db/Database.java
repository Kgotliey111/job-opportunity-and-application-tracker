package com.jobtracker.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // File-based H2 database -- data survives restarts, lives in ./data/
    private static final String URL = "jdbc:h2:file:./data/jobtracker";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Creates the applications table if it doesn't already exist.
    // Call this once when the app starts.
    public static void init() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS applications (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                job_title VARCHAR(255) NOT NULL,
                company VARCHAR(255) NOT NULL,
                status VARCHAR(50) NOT NULL DEFAULT 'Saved',
                date_applied VARCHAR(20),
                deadline VARCHAR(20),
                notes VARCHAR(2000),
                source_url VARCHAR(1000)
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
            System.out.println("Database ready at ./data/jobtracker");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
