package com.jobtracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobApplicationTest {

    @Test
    void constructorSetsAllFieldsCorrectly() {
        JobApplication app = new JobApplication(
                1L, "Backend Developer", "Acme Corp", "Applied",
                "2026-09-20", "2026-10-01", "Referred by a friend",
                "https://example.com/job/123"
        );

        assertEquals(1L, app.getId());
        assertEquals("Backend Developer", app.getJobTitle());
        assertEquals("Acme Corp", app.getCompany());
        assertEquals("Applied", app.getStatus());
        assertEquals("2026-09-20", app.getDateApplied());
        assertEquals("2026-10-01", app.getDeadline());
        assertEquals("Referred by a friend", app.getNotes());
        assertEquals("https://example.com/job/123", app.getSourceUrl());
    }

    @Test
    void settersUpdateFieldsCorrectly() {
        JobApplication app = new JobApplication();

        app.setJobTitle("Frontend Developer");
        app.setCompany("Widgets Inc");
        app.setStatus("Interview");

        assertEquals("Frontend Developer", app.getJobTitle());
        assertEquals("Widgets Inc", app.getCompany());
        assertEquals("Interview", app.getStatus());
    }

    @Test
    void noArgConstructorLeavesFieldsNull() {
        JobApplication app = new JobApplication();

        assertNull(app.getId());
        assertNull(app.getJobTitle());
        assertNull(app.getStatus());
    }
}