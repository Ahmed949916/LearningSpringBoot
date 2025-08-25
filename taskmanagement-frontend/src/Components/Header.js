import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import {
  Box, Typography, Button, Drawer, IconButton,
  List, ListItem, ListItemButton, ListItemText, Divider
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import { getSelf } from '../services/api';
import { useAuth } from '../Context/AuthContext';
import CustomButton from './CustomButton';

const Header = () => {
  const [userEmail, setUserEmail] = useState('');
  const [userRole, setUserRole] = useState('USER');
  const [drawerOpen, setDrawerOpen] = useState(false);
  const { token, logout } = useAuth();

  useEffect(() => {
    const fetchUserData = async () => {
      if (!token) return;
      try {
        const userData = await getSelf();
        if (userData?.user?.email) setUserEmail(userData.user.email);
        if (userData?.user?.role) setUserRole(userData.user.role);
      } catch {}
    };
    fetchUserData();
  }, [token]);

  const navLinks = [
    { to: '/tasks', label: 'Tasks', show: true },
    { to: '/users', label: 'Users', show: userRole === 'ADMIN' },
    { to: '/profile', label: 'Profile', show: true }
  ].filter(i => i.show);

  return (
    <Box
      component="header"
      sx={{
        backgroundColor: 'primary.main',
        px: { xs: 2, md: 4 },
        py: 2,
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        position: 'relative'
      }}
    >
      <Typography
        variant="h6"
        sx={{ color: 'primary.textLight', fontWeight: 700, fontSize: { xs: '1.3rem', md: '1.5rem' } }}
      >
        Task Management
      </Typography>

      <Box
        sx={{
          display: { xs: 'none', md: 'flex' },
          alignItems: 'center',
          gap: 3
        }}
      >
        <Box component="nav">
          <Box
            component="ul"
            sx={{ display: 'flex', listStyle: 'none', m: 0, p: 0, gap: 2 }}
          >
            {navLinks.map(({ to, label }) => (
              <Box component="li" key={to}>
                <Link
                  to={to}
                  style={{ color: 'primary.textLight', textDecoration: 'none', fontWeight: 500 }}
                >
                  {label}
                </Link>
              </Box>
            ))}
          </Box>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Typography variant="body2" sx={{ color: 'primary.textLight' }}>
            {userEmail}
          </Typography>
          <CustomButton
            onClick={logout}
          >
            Logout
          </CustomButton>
        </Box>
      </Box>

      <IconButton
        onClick={() => setDrawerOpen(true)}
        sx={{ display: { xs: 'inline-flex', md: 'none' }, color: 'primary.textLight' }}
        aria-label="menu"
      >
        <MenuIcon />
      </IconButton>

      <Drawer
        anchor="right"
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
        ModalProps={{ keepMounted: true }}
        PaperProps={{ sx: { width: 280 } }}
      >
        <Box sx={{ p: 2, backgroundColor: '#004030' }}>
          <Typography variant="h6" sx={{ color: 'primary.textLight', fontWeight: 700 }}>
            Task Management
          </Typography>
          <Typography variant="body2" sx={{ color: 'primary.textLight', opacity: 0.85 }}>
            {userEmail}
          </Typography>
        </Box>
        <Divider />
        <Box sx={{display: 'flex', flexDirection: 'column', height: '100%',justifyContent: 'space-between' }}>

        <List sx={{ p: 0 }}>
          {navLinks.map(({ to, label }) => (
            <ListItem key={to} disablePadding>
              <ListItemButton
                component={Link}
                to={to}
                onClick={() => setDrawerOpen(false)}
                sx={{
                  cursor: 'pointer',
                
                }}
              >
                <ListItemText primary={label} sx={{ fontWeight: 500 ,cursor: 'pointer'}} />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
        <Box>

        <Divider />
        <Box sx={{ p: 2, backgroundColor: 'primary.lightMain' }}>
          <Button
            onClick={() => {
              logout();
              setDrawerOpen(false);
            }}
            sx={{
              width: '100%',
              backgroundColor: 'primary.lightMain',
              color: 'primary.textLight',
            }}
            >
            Logout
          </Button>
              </Box>
            </Box>
        </Box>
      </Drawer>
    </Box>
  );
};

export default Header;
