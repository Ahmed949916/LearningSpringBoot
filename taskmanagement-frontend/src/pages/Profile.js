import React, { useEffect, useState } from 'react';
import { getSelf } from '../services/api';
import { 
  Card, 
  CardContent, 
  Typography, 
  Avatar, 
  CircularProgress, 
  Box, 
  Alert 
} from '@mui/material';

const Profile = () => {
  const [user, setUser] = useState(null);
  const [photoLink, setPhotoLink] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const data = await getSelf();
       
        setUser(data.user);
        setPhotoLink(data.photoLink);
      
      } catch (err) {
        setError('Failed to fetch user profile');
      }
    };
    fetchUser();
  }, []);

  if (error) {
    return <Alert severity="error">{error}</Alert>;
  }

  if (!user) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="40vh">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box display="flex" justifyContent="center" mt={5}>
      <Card 
        sx={{ 
          maxWidth: 400,
          width:"100%", 
          borderRadius: 4,
          p: 2, 
          textAlign: 'center', 
          background: '#f9f9f9'
        }}
      >
    <Avatar 
  src={photoLink}  
  alt={user.username}     
  sx={{ 
    bgcolor: '#1976d2', 
    width: 80, 
    height: 80, 
    fontSize: 28, 
    margin: 'auto', 
    mb: 1 
  }}
>
  {(!photoLink && user.name) ? user.name.charAt(0) : null}
</Avatar>

        <CardContent>
          <Typography variant="h5" fontWeight="bold">
            {user.name}
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 1 }}>
            {user.username}
          </Typography>
          <Typography 
            variant="body2" 
            sx={{ 
              background: '#E3F2FD', 
              color: '#1976d2', 
              display: 'inline-block', 
              px: 2, 
              py: 0.5, 
              borderRadius: 3, 
              fontWeight: 600 
            }}
          >
            {user.role=== 'ROLE_ADMIN' ? 'Administrator' : 'User'}
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Profile;
