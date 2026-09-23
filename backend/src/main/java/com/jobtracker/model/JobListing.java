package com.jobtracker.model;

// Represents one result from the Arbeitnow job-search API.
// Only carries the fields our frontend actually displays.
import java.util.List;

public class JobListing {

    private String title;
    private String companyName;
    private String location;
    private boolean remote;
    private String url;
    private String description;      // short, truncated preview for cards
    private String fullDescription;  // untruncated, for the details view
    private List<String> tags;       // skill/tech tags, e.g. ["java", "sql"]
    private List<String> jobTypes;   // e.g. ["full_time", "internship"]

    public JobListing() {
    }

    public JobListing(String title, String companyName, String location, boolean remote,
                       String url, String description, String fullDescription,
                       List<String> tags, List<String> jobTypes) {
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.remote = remote;
        this.url = url;
        this.description = description;
        this.fullDescription = fullDescription;
        this.tags = tags;
        this.jobTypes = jobTypes;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isRemote() { return remote; }
    public void setRemote(boolean remote) { this.remote = remote; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFullDescription() { return fullDescription; }
    public void setFullDescription(String fullDescription) { this.fullDescription = fullDescription; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<String> getJobTypes() { return jobTypes; }
    public void setJobTypes(List<String> jobTypes) { this.jobTypes = jobTypes; }
}
