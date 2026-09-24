package com.jobtracker.repository;

import com.jobtracker.db.Database;
import com.jobtracker.model.JobApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for JobApplicationRepository.
 * These run against the real H2 database file (same one the app uses),
 * so each test cleans up the rows it creates in @AfterEach.
 */
class JobApplicationRepositoryTest {

    private final JobApplicationRepository repository = new JobApplicationRepository();

    @BeforeAll
    static void setUpDatabase() {
        Database.init();
    }

    @AfterEach
    void cleanUp() {
        // Remove any test rows we created so repeated test runs stay clean.
        for (JobApplication app : repository.findAll()) {
            if (app.getCompany() != null && app.getCompany().startsWith("TestCorp")) {
                repository.delete(app.getId());
            }
        }
    }

    @Test
    void saveAssignsAnIdAndPersistsTheApplication() {
        JobApplication app = new JobApplication(
                null, "QA Engineer", "TestCorp-Save", "Saved",
                null, null, null, null
        );

        JobApplication saved = repository.save(app);

        assertNotNull(saved.getId(), "Saved application should be given a generated id");

        boolean found = repository.findAll().stream()
                .anyMatch(a -> a.getId().equals(saved.getId()));
        assertTrue(found, "Saved application should appear in findAll()");
    }

    @Test
    void saveDefaultsStatusToSavedWhenNoneProvided() {
        JobApplication app = new JobApplication(
                null, "DevOps Engineer", "TestCorp-Default", null,
                null, null, null, null
        );

        JobApplication saved = repository.save(app);

        assertEquals("Saved", saved.getStatus());
    }

    @Test
    void updateChangesStatusForAnExistingApplication() {
        JobApplication app = new JobApplication(
                null, "Data Analyst", "TestCorp-Update", "Saved",
                null, null, null, null
        );
        JobApplication saved = repository.save(app);

        saved.setStatus("Interview");
        boolean success = repository.update(saved.getId(), saved);

        assertTrue(success, "Update should report success for an existing id");

        JobApplication updated = repository.findAll().stream()
                .filter(a -> a.getId().equals(saved.getId()))
                .findFirst()
                .orElseThrow();
        assertEquals("Interview", updated.getStatus());
    }

    @Test
    void updateReturnsFalseForANonExistentId() {
        JobApplication fake = new JobApplication(
                999999L, "Ghost Role", "TestCorp-Ghost", "Saved",
                null, null, null, null
        );

        boolean success = repository.update(999999L, fake);

        assertFalse(success, "Updating a non-existent id should return false");
    }

    @Test
    void deleteRemovesTheApplication() {
        JobApplication app = new JobApplication(
                null, "Intern", "TestCorp-Delete", "Saved",
                null, null, null, null
        );
        JobApplication saved = repository.save(app);

        boolean deleted = repository.delete(saved.getId());

        assertTrue(deleted);
        boolean stillPresent = repository.findAll().stream()
                .anyMatch(a -> a.getId().equals(saved.getId()));
        assertFalse(stillPresent, "Deleted application should no longer appear in findAll()");
    }
}