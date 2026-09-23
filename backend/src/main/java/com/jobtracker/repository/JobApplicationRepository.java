package com.jobtracker.repository;

import com.jobtracker.db.Database;
import com.jobtracker.model.JobApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationRepository {

    public List<JobApplication> findAll() {
        String sql = "SELECT * FROM applications ORDER BY id DESC";
        List<JobApplication> results = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch applications", e);
        }
        return results;
    }

    public JobApplication save(JobApplication app) {
        String sql = """
            INSERT INTO applications (job_title, company, status, date_applied, deadline, notes, source_url)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, app.getJobTitle());
            stmt.setString(2, app.getCompany());
            stmt.setString(3, app.getStatus() != null ? app.getStatus() : "Saved");
            stmt.setString(4, app.getDateApplied());
            stmt.setString(5, app.getDeadline());
            stmt.setString(6, app.getNotes());
            stmt.setString(7, app.getSourceUrl());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    app.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save application", e);
        }
        return app;
    }

    // Partial update -- mainly used to move an application to a new status
    // (Saved -> Applied -> Interview -> Offer -> Accepted/Rejected).
    public boolean update(Long id, JobApplication app) {
        String sql = """
            UPDATE applications
            SET job_title = ?, company = ?, status = ?, date_applied = ?, deadline = ?, notes = ?
            WHERE id = ?
            """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, app.getJobTitle());
            stmt.setString(2, app.getCompany());
            stmt.setString(3, app.getStatus());
            stmt.setString(4, app.getDateApplied());
            stmt.setString(5, app.getDeadline());
            stmt.setString(6, app.getNotes());
            stmt.setLong(7, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update application", e);
        }
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM applications WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete application", e);
        }
    }

    private JobApplication mapRow(ResultSet rs) throws SQLException {
        return new JobApplication(
            rs.getLong("id"),
            rs.getString("job_title"),
            rs.getString("company"),
            rs.getString("status"),
            rs.getString("date_applied"),
            rs.getString("deadline"),
            rs.getString("notes"),
            rs.getString("source_url")
        );
    }
}
