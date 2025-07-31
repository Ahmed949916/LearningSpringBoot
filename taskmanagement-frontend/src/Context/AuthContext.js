import { createContext, useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [userId, setUserId] = useState(localStorage.getItem('userId'));
 
  const navigate = useNavigate();

  const loginWithGoogle = () => {
    window.location.href = 'http://localhost:8080/oauth2/authorization/google';
  };

  const handleUserId = (id) => {
    setUserId(id);
    localStorage.setItem('userId', id);
  };

  const handleOAuthRedirect = async (token) => {
    localStorage.setItem('token', token);
    setToken(token);
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
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
      token, 
      loginWithGoogle, 
      handleOAuthRedirect, 
      logout ,
      userId,
      handleUserId
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);