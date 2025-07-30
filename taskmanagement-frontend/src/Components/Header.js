import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getSelf } from '../services/api';
import { useAuth } from '../Context/AuthContext';
import './Header.css';
import CustomButton from './CustomButton';

const Header = () => {
  const [userEmail, setUserEmail] = useState('');
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const { token, logout } = useAuth();

  useEffect(() => {
    const fetchUserData = async () => {
      if (token) {
        try {
          const userData = await getSelf();
          if (userData && userData.email) {
            setUserEmail(userData.email);
          }
        } catch (error) {
          console.error('Failed to fetch user data:', error);
        }
      }
    };

    fetchUserData();
  }, [token]);

  const toggleMobileMenu = () => {
    setIsMobileMenuOpen(!isMobileMenuOpen);
  };

  const closeMobileMenu = () => {
    setIsMobileMenuOpen(false);
  };

  return (
        <header className="header">
            <div className="header-left">
                <h1>Task Management</h1>
            </div>
            
           
            <div className="header-right desktop-nav">
                <nav>
                    <ul className="nav-links">
                        <li><Link to="/tasks">Tasks</Link></li>
                        {/* <li><Link to="/users">Users</Link></li> */}
                        <li><Link to="/profile">Profile</Link></li>
                    </ul>
                </nav>
                <div className="user-info">
                    <span className="user-email">{userEmail}</span>
                   <CustomButton onClick={() => { logout() }} >
                       Logout
                   </CustomButton>
                </div>
            </div>

            
            <div className="mobile-menu-toggle" onClick={toggleMobileMenu}>
                <span className={`burger-line ${isMobileMenuOpen ? 'active' : ''}`}></span>
                <span className={`burger-line ${isMobileMenuOpen ? 'active' : ''}`}></span>
                <span className={`burger-line ${isMobileMenuOpen ? 'active' : ''}`}></span>
            </div>

       
            <div className={`mobile-nav ${isMobileMenuOpen ? 'open' : ''}`}>
                <nav>
                    <ul className="mobile-nav-links">
                        <li><Link to="/tasks" onClick={closeMobileMenu}>Tasks</Link></li>
                        {/* <li><Link to="/users" onClick={closeMobileMenu}>Users</Link></li> */}
                        <li><Link to="/profile" onClick={closeMobileMenu}>Profile</Link></li>
                    </ul>
                </nav>
                <div className="mobile-user-info">
                    <span className="user-email">{userEmail}</span>
                    <button onClick={() => { logout(); closeMobileMenu(); }} className="logout-btn">Logout</button>
                </div>
            </div>

          
            {isMobileMenuOpen && <div className="mobile-menu-overlay" onClick={closeMobileMenu}></div>}
        </header>
    );
};

export default Header;