
import { createContext, useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const API_ORIGIN =
  (process.env.REACT_APP_API_BASE_URL?.replace(/\/$/, '')) || '';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const navigate = useNavigate();



  const loginWithGoogle = () => {
    window.location.href = `${API_ORIGIN}/oauth2/authorization/google`;
  };



  const handleOAuthRedirect = async (t) => {
    localStorage.setItem('token', t);
    setToken(t);
    axios.defaults.headers.common['Authorization'] = `Bearer ${t}`;
    navigate('/tasks');
  };

  const logout = () => {
    localStorage.removeItem('token');

    delete axios.defaults.headers.common['Authorization'];
    setToken(null);

    navigate('/');
  };

  return (
    <AuthContext.Provider value={{
      token, loginWithGoogle, handleOAuthRedirect, logout
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
