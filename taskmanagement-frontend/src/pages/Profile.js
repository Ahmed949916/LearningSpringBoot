import React, { useEffect, useState } from 'react';
import { getSelf } from '../services/api';
import { Typography } from '@mui/material';

const Profile = () => {
  const [user, setUser] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const data = await getSelf();
        setUser(data);
      } catch (err) {
        setError('Failed to fetch user profile');
      }
    };

    fetchUser();
  }, []);

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <h3>Profile</h3>
      <Typography>{user.name}</Typography>
      
        <Typography>Email: {user.username}</Typography>
    
     
    </div>
  );
};

export default Profile;
