
import { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { fetchCsrfToken } from '../services/api';

const API_ORIGIN =
  (process.env.REACT_APP_API_BASE_URL?.replace(/\/$/, '')) || '';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userId, setUserId] = useState(localStorage.getItem('userId'));
  const navigate = useNavigate();

  useEffect(() => {
    (async () => {
      try { await fetchCsrfToken(); } 
      catch (e) { console.error('Failed to initialize CSRF token:', e); }
    })();
  }, []);

  const loginWithGoogle = () => {
    window.location.href = `${API_ORIGIN}/oauth2/authorization/google`;
  };

  const handleUserId = (id) => {
    setUserId(id);
    localStorage.setItem('userId', id);
  };

  const handleOAuthRedirect = async (t) => {
    localStorage.setItem('token', t);
    setToken(t);
    axios.defaults.headers.common['Authorization'] = `Bearer ${t}`;
    navigate('/tasks');
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    delete axios.defaults.headers.common['Authorization'];
    setToken(null);
    setUserId(null);
    navigate('/');
  };

  return (
    <AuthContext.Provider value={{
      token, loginWithGoogle, handleOAuthRedirect, logout, userId, handleUserId
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
