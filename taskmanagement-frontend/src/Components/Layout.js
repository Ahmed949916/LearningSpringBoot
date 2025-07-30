import React from 'react';
import { useLocation } from 'react-router-dom';
import Header from './Header';
import { useAuth } from '../Context/AuthContext';

const Layout = ({ children }) => {
  const location = useLocation();
  const { token } = useAuth();
 
  const hideHeader = ['/login', '/', '/oauth2/redirect'].includes(location.pathname) || !token;

  return (
    <div className="app-layout">
      {!hideHeader && <Header />}
      <main className={hideHeader ? 'main-content-full' : 'main-content-with-header'}>
        {children}
      </main>
    </div>
  );
};

export default Layout;
