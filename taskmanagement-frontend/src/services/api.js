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

export const getSelf = async () => (await api.get("/user/profile")).data;

export default api;
