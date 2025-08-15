import React, { useEffect, useState } from 'react';
import { getSelf } from '../services/api';
import {
  Card, CardContent, Typography, Avatar,
  CircularProgress, Box, Alert
} from '@mui/material';

const Profile = () => {
  const [user, setUser] = useState(null);
  const [photoLink, setPhotoLink] = useState(null);
  const [avatarSrc, setAvatarSrc] = useState(undefined);
  const [error, setError] = useState(null);

  useEffect(() => {
    (async () => {
      try {
        const data = await getSelf(); 
        setUser(data.user);
        setPhotoLink(data.photoLink || null);
      } catch {
        setError('Failed to fetch user profile');
      }
    })();
  }, []);

 
  useEffect(() => {
    setAvatarSrc(photoLink || undefined);
  }, [photoLink]);

  if (error) return <Alert severity="error">{error}</Alert>;
  if (!user) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="40vh">
        <CircularProgress />
      </Box>
    );
  }


  const email = user.username || '';
  const displayName = email.includes('@') ? email.split('@')[0] : email;
  const initials = displayName ? displayName.charAt(0).toUpperCase() : '?';

  return (
    <Box display="flex" justifyContent="center" mt={5}>
      <Card
        sx={{
          maxWidth: 400, width: '100%', borderRadius: 4, p: 2,
          textAlign: 'center', background: '#f9f9f9'
        }}
      >
        <Avatar
          src={avatarSrc}
          alt={email}
          imgProps={{
            referrerPolicy: 'no-referrer',
            loading: 'lazy',
            crossOrigin: 'anonymous',
            onError: () => setAvatarSrc(undefined) 
          }}
          sx={{ bgcolor: '#1976d2', width: 80, height: 80, fontSize: 28, m: 'auto', mb: 1 }}
        >
          {initials}
        </Avatar>

        <CardContent>
          <Typography variant="h5" fontWeight="bold">
            {displayName}
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 1 }}>
            {email}
          </Typography>
          <Typography
            variant="body2"
            sx={{
              background: '#E3F2FD', color: '#1976d2', display: 'inline-block',
              px: 2, py: 0.5, borderRadius: 3, fontWeight: 600
            }}
          >
            {user.role === 'ROLE_ADMIN' ? 'Administrator' : 'User'}
          </Typography>
        </CardContent>
      </Card>
    </Box>
  );
};

export default Profile;
