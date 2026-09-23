package com.jobtracker;

import com.jobtracker.db.Database;
import com.jobtracker.model.JobApplication;
import com.jobtracker.repository.JobApplicationRepository;
import com.jobtracker.service.ArbeitnowService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {

    public static void main(String[] args) {
        Database.init();

        JobApplicationRepository repository = new JobApplicationRepository();
        ArbeitnowService arbeitnowService = new ArbeitnowService();

        Javalin app = Javalin.create(config -> {
            // Serves everything in src/main/resources/public as the frontend --
            // no separate frontend server or CORS setup needed.
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7000);

        // ----- Job applications (your own tracked data) -----

        app.get("/api/applications", ctx -> {
            ctx.json(repository.findAll());
        });

        app.post("/api/applications", ctx -> {
            JobApplication app1 = ctx.bodyAsClass(JobApplication.class);
            JobApplication saved = repository.save(app1);
            ctx.status(201).json(saved);
        });

        app.put("/api/applications/{id}", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            JobApplication updated = ctx.bodyAsClass(JobApplication.class);
            boolean success = repository.update(id, updated);
            if (success) {
                ctx.status(204);
            } else {
                ctx.status(404).json(java.util.Map.of("error", "Application not found"));
            }
        });

        app.delete("/api/applications/{id}", ctx -> {
            Long id = Long.valueOf(ctx.pathParam("id"));
            boolean success = repository.delete(id);
            if (success) {
                ctx.status(204);
            } else {
                ctx.status(404).json(java.util.Map.of("error", "Application not found"));
            }
        });

        // ----- Job search (external integration: Arbeitnow) -----

        app.get("/api/jobs/search", ctx -> {
            String keyword = ctx.queryParam("keyword");
            String location = ctx.queryParam("location");
            String jobType = ctx.queryParam("jobType");
            boolean remoteOnly = "true".equals(ctx.queryParam("remoteOnly"));
            ctx.json(arbeitnowService.search(keyword, location, jobType, remoteOnly));
        });

        System.out.println("Job tracker running at http://localhost:7000");
    }
}
