
import axios from 'axios';


const API_ORIGIN =
  process.env.REACT_APP_API_BASE_URL?.replace(/\/$/, '') || ''; 


const API_BASE_URL = `${API_ORIGIN}/api`;


const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
});

let csrfToken = null;

export const fetchCsrfToken = async () => {
  try {
    const res = await axios.get(`${API_BASE_URL}/csrf`, { withCredentials: true });
    csrfToken = res.data?.token || res.headers['x-xsrf-token'] || null;
    return csrfToken;
  } catch (err) {
    console.error('Error fetching CSRF token:', err);
    throw err;
  }
};


api.interceptors.request.use(
  async (config) => {
    const token = localStorage.getItem('token');
    if (token) config.headers['Authorization'] = `Bearer ${token}`;

    if (['post', 'put', 'patch', 'delete'].includes((config.method || '').toLowerCase())) {
      if (!csrfToken) await fetchCsrfToken();
      if (csrfToken) config.headers['X-XSRF-TOKEN'] = csrfToken;
    }
    return config;
  },
  (error) => Promise.reject(error)
);



export const logout = async () => api.post('/perform_logout');

export const getTasks = async () => (await api.get('/task')).data;
export const createTask = async (taskData) => (await api.post('/task', taskData)).data;
export const updateTask = async (id, taskData) => (await api.patch(`/task/${id}`, taskData)).data;
export const deleteTask = async (id) => api.delete(`/task/${id}`);

export const getUsers = async () => (await api.get('/user')).data;
export const deleteUser = async (id) => api.delete(`/user/${id}`);

export const getSelf = async () => (await api.get('/user/profile')).data;

export default api;
