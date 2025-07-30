import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle 401 errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);


 
export const register = async (userData) => {
  const response = await api.post('/api/user/register', userData);
  return response.data;
};
export const login = async (username, password) => {
  try {
 
    const authAxios = axios.create();
    
    const response = await authAxios.post(
      'http://localhost:8080/perform_login',
      new URLSearchParams({ username, password }).toString(),
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        withCredentials: true
      }
    );

    if (response.status === 200 && response.data.access_token) {
      // Store the token
      localStorage.setItem('token', response.data.access_token);
      
      // Set default Authorization header for future requests
      axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.access_token}`;
      
      return response.data;
    }
    throw new Error('Authentication failed');
  } catch (error) {
    console.error('Login error:', error);
    throw error;
  }
};
export const logout = async () => {
  return api.post('/perform_logout');
};

export const getTasks = async () => {
  const response = await api.get('/task');
  return response.data;
};

export const createTask = async (taskData) => {
  const response = await api.post('/task', taskData);
  return response.data;
};

export const updateTask = async (id, taskData) => {
  const response = await api.patch(`/task/${id}`, taskData);
  return response.data;
};

export const deleteTask = async (id) => {
  return api.delete(`/task/${id}`);
};

export const getUsers = async () => {
  const response = await api.get('/user');
  return response.data;
};

export const deleteUser = async (id) => {
  return api.delete(`/user/${id}`);
};

export default api;