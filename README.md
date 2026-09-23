# Job Opportunity and Application Tracker

Javalin (Java) backend + plain HTML/CSS/JS frontend. No React, no Spring Boot,
no card required for anything.

## What's here

- **Search real jobs** via [Arbeitnow's free public API](https://arbeitnow.com/blog/job-board-api/)
  — no key, no signup, no billing.
- **Save any listing** to your own tracked applications with one click.
- **Track status** through Saved → Applied → Interview → Offer → Accepted/Rejected.
- **Add applications manually** too, for jobs you found elsewhere.
- Data persists in a local H2 file database (`backend/data/`).

## System integration story (for your write-up)

```
Browser (HTML/CSS/JS)
      |
      v
Javalin REST API  ---->  Arbeitnow job board API (external system)
      |
      v
H2 database (your own data)
```

Two integrations: your app <-> Arbeitnow (external job data), and your app <->
its own database (persisted application tracking). Email reminders can be
added later as a third integration (see "What's next" below) without
touching what's already built.

## Running it

Requires Java 17+ and Maven.

```bash
cd backend
mvn compile exec:java -Dexec.mainClass=com.jobtracker.Main
```

Or build a runnable jar and run that:

```bash
cd backend
mvn package
java -jar target/job-opportunity-and-application-tracker.jar
```

Then open **http://localhost:7000** — the backend serves the frontend
directly, so there's nothing else to start.

## Try it

1. Go to "Find jobs", search a keyword like `java` or `frontend`.
2. Click "Save to my applications" on one you like.
3. Go to "My applications" — it's there. Change its status with the dropdown.
4. Restart the server — your data is still there (it's in `backend/data/`).

## What's next

- **AI skill-matching** (stretch goal, as planned): add a text box where the
  user pastes their skills, then compare against a saved job's description
  and highlight matches/gaps. This slots in as a new endpoint
  (`POST /api/jobs/match`) without touching existing code.
- **Email reminders**: a service that checks `deadline` dates and sends a
  reminder email. Brevo (formerly Sendinblue) has a free tier that doesn't
  require a card to sign up — worth checking first before committing to it.
- **Login/multiple users**: right now all applications are shared in one
  table. Adding a `user_id` column and simple session auth would let you
  demo multi-user support if your rubric wants it.

## Notes

- This was written in a sandboxed environment that can't reach Maven
  Central, so **it wasn't compiled here** — run `mvn compile exec:java ...`
  yourself to build it for the first time. If anything doesn't compile,
  paste me the error and I'll fix it.
- The Arbeitnow API is mostly Europe/remote-focused — worth checking search
  results match what you expect for your demo before presenting.
