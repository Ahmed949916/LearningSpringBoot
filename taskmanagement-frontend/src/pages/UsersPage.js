import { useState, useEffect } from 'react';
import { useAuth } from '../Context/AuthContext.js';
import axios from 'axios';
import { 
  Box, Typography, Table, TableBody, TableCell, 
  TableContainer, TableHead, TableRow, Paper, IconButton
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { deleteUser, getUsers } from '../services/api.js';

const UsersPage = () => {
  const { token } = useAuth();
  const [users, setUsers] = useState([]);
 
  useEffect(() => {
    if (token) {
      getUsers().then(setUsers);
    }
    // eslint-disable-next-line
  }, [token]);

 

  const handleDelete = async (id) => {
    try {
     await deleteUser(id);
      getUsers().then(setUsers);
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  if (!token) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h6">Please login to view users</Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" sx={{ mb: 3 }}>Users</Typography>
      
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>Username</TableCell>
              <TableCell>Role</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.userId}>
                <TableCell>{user.userId}</TableCell>
                <TableCell>{user.username}</TableCell>
                <TableCell>{user.role}</TableCell>
                <TableCell>
                  <IconButton onClick={() => handleDelete(user.userId)}>
                    <DeleteIcon sx={{ color: 'error.main' }} />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default UsersPage;