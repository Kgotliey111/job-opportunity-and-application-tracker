const API_BASE = "/api";

async function getApplications() {
  const res = await fetch(`${API_BASE}/applications`);
  if (!res.ok) throw new Error("Failed to load applications");
  return res.json();
}

async function createApplication(app) {
  const res = await fetch(`${API_BASE}/applications`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(app),
  });
  if (!res.ok) throw new Error("Failed to save application");
  return res.json();
}

async function updateApplication(id, app) {
  const res = await fetch(`${API_BASE}/applications/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(app),
  });
  if (!res.ok) throw new Error("Failed to update application");
}

async function deleteApplication(id) {
  const res = await fetch(`${API_BASE}/applications/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Failed to delete application");
}

async function searchJobs({ keyword, location, jobType, remoteOnly } = {}) {
  const params = new URLSearchParams();
  if (keyword) params.set("keyword", keyword);
  if (location) params.set("location", location);
  if (jobType) params.set("jobType", jobType);
  if (remoteOnly) params.set("remoteOnly", "true");

  const query = params.toString();
  const url = query ? `${API_BASE}/jobs/search?${query}` : `${API_BASE}/jobs/search`;
  const res = await fetch(url);
  if (!res.ok) throw new Error("Failed to search jobs");
  return res.json();
}
