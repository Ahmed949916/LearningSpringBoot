import axios from "axios";


const API_BASE_URL = "api";

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
 
  xsrfCookieName: "XSRF-TOKEN",
  xsrfHeaderName: "X-XSRF-TOKEN",
});


api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers["Authorization"] = `Bearer ${token}`;
  return config;
});


export const logout = async () => api.post("/perform_logout");

export const getTasks = async () => (await api.get("/task")).data;
export const createTask = async (taskData) => (await api.post("/task", taskData)).data;
export const updateTask = async (id, taskData) => (await api.patch(`/task/${id}`, taskData)).data;
export const deleteTask = async (id) => api.delete(`/task/${id}`);

export const getUsers = async () => (await api.get("/user")).data;
export const deleteUser = async (id) => api.delete(`/user/${id}`);
export const getSimpleResponse =async (prompt) => (await api.post("/gemini/simple-response", { prompt })).data;
 
export async function streamResponse(prompt, onChunk) {
  const token = localStorage.getItem("token");
  const xsrfMatch = document.cookie.match(/(?:^|;\s*)XSRF-TOKEN=([^;]+)/);
  const xsrf = xsrfMatch ? decodeURIComponent(xsrfMatch[1]) : "";

  const res = await fetch(`/api/gemini/stream?prompt=${encodeURIComponent(prompt)}`, {
    method: "GET",
    credentials: "include",
    headers: {
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(xsrf ? { "X-XSRF-TOKEN": xsrf } : {}),
    },
  });
  if (!res.ok || !res.body) throw new Error("Stream failed");

  const reader = res.body.getReader();
  const decoder = new TextDecoder();
  let buffer = "";

  while (true) {
    const { value, done } = await reader.read();
    if (done) break;

    buffer += decoder.decode(value, { stream: true });

    // Split SSE events by the blank line separator
    let events = buffer.split(/\r?\n\r?\n/);
    buffer = events.pop() || "";

    for (const evt of events) {
      // Collect only data: lines; KEEP their leading spaces
      const dataLines = evt
        .split(/\r?\n/)
        .filter((l) => l.startsWith("data:"))
        .map((l) => l.replace(/^data:/, "")); // <-- no \s? here

      if (dataLines.length) {
        const payload = dataLines.join("\n");
        if (payload === "[DONE]") return;
        onChunk(payload);
      }
    }
  }
}


export const getSelf = async (prompt) => (await api.get("/user/profile", { params: { prompt } })).data;
export const createUser =async (userData) => (await api.post("/user", userData)).data;
export default api;
