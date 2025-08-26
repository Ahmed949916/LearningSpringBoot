import { useRef, useEffect, useState } from 'react';
import { 
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Alert,
  MenuItem,
  CircularProgress
} from '@mui/material';
import { createUser } from '../../services/api.js';
import CustomButton from '../CustomButton.js';
import CustomInput from "../CustomInput.js";
import CustomSelect from "../CustomSelect.js";

const CreateUserModal = ({ open, onClose, onUserCreated }) => {
  const usernameRef = useRef(null);
  const passwordRef = useRef(null);
  const roleRef = useRef(null);
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (open) setTimeout(() => usernameRef.current?.focus(), 0);
  }, [open]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const username = usernameRef.current?.value?.trim() || '';
    const password = passwordRef.current?.value?.trim() || '';
    const role = roleRef.current?.value || 'USER';
    
    if (!username || !password) {
      setError('Please fill in all fields');
      return;
    }

    setLoading(true);
    setError('');

    try {
      await createUser({ username, password, role });
      onUserCreated(); 
      onClose(); 
    } catch (error) {
      setError(error.response?.data?.message || 'Error creating user');
    } finally {
      setLoading(false);
    }
  };

  const handleClose = () => {
    setError('');
    onClose();
  };

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{fontWeight:"bold",textAlign:"center"}}>Create New User</DialogTitle>

      <form onSubmit={handleSubmit}>
        <DialogContent sx={{ minWidth: 400 }}>
          {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
          
          <CustomInput
            label="Username"
            name="username"
            inputRef={usernameRef}
            required
            sx={{ mt: 1 }}
          />
          
          <CustomInput
            label="Password"
            name="password"
            type="password"
            inputRef={passwordRef}
            required
          />
          
          <CustomSelect
            label="Role"
            name="role"
            defaultValue="USER"
            inputRef={roleRef}
            fullWidth
          >
            <MenuItem value="USER">User</MenuItem>
            <MenuItem value="ADMIN">Admin</MenuItem>
          </CustomSelect>
 
        </DialogContent>
        
        <DialogActions sx={{ p: 2 }}>
          <CustomButton sx={{backgroundColor:"primary.textLight",color:"primary.lightMain"}} onClick={handleClose} disabled={loading}>
            Cancel
          </CustomButton>
          <CustomButton type="submit" disabled={loading}>
            {loading ? <CircularProgress size={24} /> : 'Create User'}
          </CustomButton>
          
        </DialogActions>
      </form>
    </Dialog>
  );
};

export default CreateUserModal;