package com.jobtracker.model;

public class JobApplication {

    private Long id;
    private String jobTitle;
    private String company;
    private String status;       // Saved, Applied, Interview, Offer, Accepted, Rejected
    private String dateApplied;  // ISO date string, e.g. "2026-09-20"
    private String deadline;     // ISO date string, may be null
    private String notes;
    private String sourceUrl;    // link back to the original job posting, if saved from search

    public JobApplication() {
    }

    public JobApplication(Long id, String jobTitle, String company, String status,
                           String dateApplied, String deadline, String notes, String sourceUrl) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.company = company;
        this.status = status;
        this.dateApplied = dateApplied;
        this.deadline = deadline;
        this.notes = notes;
        this.sourceUrl = sourceUrl;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDateApplied() { return dateApplied; }
    public void setDateApplied(String dateApplied) { this.dateApplied = dateApplied; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
}
