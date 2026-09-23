package com.jobtracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.model.JobListing;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArbeitnowService {

    // Arbeitnow's public job board API. No API key, no signup, no billing --
    // see https://arbeitnow.com/blog/job-board-api/
    // The API itself only supports a "search" keyword param, so location,
    // job type, and remote-only are filtered here in Java after fetching.
    private static final String BASE_URL = "https://arbeitnow.com/api/job-board-api";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Searches Arbeitnow's job board, then narrows results down by the
     * optional filters. Any filter left null/blank is skipped.
     *
     * @param keyword    matched against title/description by Arbeitnow itself
     * @param location   substring match against the listing's location (case-insensitive)
     * @param jobType    matched against the listing's job_types, e.g. "full_time"
     * @param remoteOnly if true, only remote listings are returned
     */
    public List<JobListing> search(String keyword, String location, String jobType, boolean remoteOnly) {
        try {
            String url = BASE_URL;
            if (keyword != null && !keyword.isBlank()) {
                String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
                url += "?search=" + encoded;
            }

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Arbeitnow API returned status " + response.statusCode());
            }

            List<JobListing> listings = parseListings(response.body());
            return applyFilters(listings, location, jobType, remoteOnly);

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to reach Arbeitnow API", e);
        }
    }

    private List<JobListing> applyFilters(List<JobListing> listings, String location, String jobType, boolean remoteOnly) {
        List<JobListing> filtered = new ArrayList<>();

        for (JobListing job : listings) {
            if (remoteOnly && !job.isRemote()) continue;

            if (location != null && !location.isBlank()) {
                String jobLocation = job.getLocation();
                if (jobLocation == null || !jobLocation.toLowerCase(Locale.ROOT)
                        .contains(location.toLowerCase(Locale.ROOT))) {
                    continue;
                }
            }

            if (jobType != null && !jobType.isBlank()) {
                boolean matches = job.getJobTypes() != null && job.getJobTypes().stream()
                    .anyMatch(t -> t.equalsIgnoreCase(jobType));
                if (!matches) continue;
            }

            filtered.add(job);
        }
        return filtered;
    }

    private List<JobListing> parseListings(String json) throws IOException {
        List<JobListing> listings = new ArrayList<>();
        JsonNode root = mapper.readTree(json);
        JsonNode data = root.get("data");

        if (data != null && data.isArray()) {
            for (JsonNode job : data) {
                String rawDescription = textOrNull(job, "description");
                String plainDescription = stripHtml(rawDescription);

                JobListing listing = new JobListing(
                    textOrNull(job, "title"),
                    textOrNull(job, "company_name"),
                    textOrNull(job, "location"),
                    job.has("remote") && job.get("remote").asBoolean(),
                    textOrNull(job, "url"),
                    truncate(plainDescription, 300),
                    plainDescription,
                    toStringList(job.get("tags")),
                    toStringList(job.get("job_types"))
                );
                listings.add(listing);
            }
        }
        return listings;
    }

    private List<String> toStringList(JsonNode arrayNode) {
        List<String> result = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                result.add(item.asText());
            }
        }
        return result;
    }

    private String textOrNull(JsonNode node, String field) {
        return node.has(field) && !node.get(field).isNull() ? node.get(field).asText() : null;
    }

    private String stripHtml(String text) {
        if (text == null) return null;
        return text.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return null;
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }
}
