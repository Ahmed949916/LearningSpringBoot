import { useState, useEffect, useRef } from 'react';
 
import { 
  Box,  Typography, Table, TableBody, TableCell, 
  TableContainer, TableHead, TableRow, IconButton,
  Dialog, DialogTitle, DialogContent, DialogActions, TextField,
  CircularProgress, Alert, Snackbar, Select, MenuItem, FormControl, InputLabel
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { getTasks, createTask, updateTask, deleteTask } from '../services/api';
import { useAuth } from '../Context/AuthContext';
import CustomButton from '../Components/CustomButton';
 
const TasksPage = () => {
  const { token } = useAuth();
  const [tasks, setTasks] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  
  const [isEdit, setIsEdit] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const currentTaskRef = useRef({ title: '', description: '', completed: false }); 

  useEffect(() => {
    if (token) {
      fetchTasks();
    }
  }, [token]);

 
  const fetchTasks = async () => {
    setLoading(true);
    try {
      const data = await getTasks();
      setTasks(data);
    } catch (error) {
      setError('Failed to fetch tasks');
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (task = null) => {
    currentTaskRef.current = task 
      ? { ...task } 
      : { title: '', description: '', completed: false };
    setIsEdit(!!task);
    setOpenDialog(true);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    if (name === 'completed') {
      currentTaskRef.current[name] = value === 'true';
    } else {
      currentTaskRef.current[name] = value;
    }
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      if (isEdit) {
        await updateTask(currentTaskRef.current.taskId, currentTaskRef.current);
        setSuccess('Task updated successfully');
      } else {
        await createTask(currentTaskRef.current);
        setSuccess('Task created successfully');
      }
      fetchTasks();
      setOpenDialog(false);
    } catch {
      setError(isEdit ? 'Failed to update task' : 'Failed to create task');
    } finally {
      setLoading(false);
    }
  };


  const handleDelete = async (id) => {
    setLoading(true);
    try {
      await deleteTask(id);
      setSuccess('Task deleted successfully');
      fetchTasks();
    } catch (error) {
      setError('Failed to delete task');
    } finally {
      setLoading(false);
    }
  };

  if (!token) {
    return (
      <Box sx={{ p: 3 }}>
        <Typography variant="h6">Please login to view tasks</Typography>
      </Box>
    );
  }
  
 
  return (
    <Box sx={{ p: 3 }}>
      {error && (
        <Snackbar open={!!error} autoHideDuration={6000} onClose={() => setError('')}>
          <Alert severity="error" onClose={() => setError('')}>{error}</Alert>
        </Snackbar>
      )}
      {success && (
        <Snackbar open={!!success} autoHideDuration={6000} onClose={() => setSuccess('')}>
          <Alert severity="success" onClose={() => setSuccess('')}>{success}</Alert>
        </Snackbar>
      )}
      
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h4">Tasks</Typography>
        <CustomButton
          onClick={() => handleOpenDialog()}
          startIcon={<AddIcon />}
        >
          Add Task
        </CustomButton>
      </Box>

      {loading ? (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer >
          <Table>
            <TableHead>
              <TableRow  sx={{ backgroundColor: '#fffcf4ff' }}>
                <TableCell sx={{fontWeight:"bold"}}>Title</TableCell>
                <TableCell sx={{fontWeight:"bold"}}>Description</TableCell>
                <TableCell sx={{fontWeight:"bold"}}>Actions</TableCell>
                <TableCell sx={{fontWeight:"bold"}}>Status</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              
              {tasks.map((task) => (
                <TableRow key={task.taskId}>
                  <TableCell>{task.title}</TableCell>
                  <TableCell>{task.description}</TableCell>
                  <TableCell>
                    {task.completed ? 'Completed' : 'Pending'}
                  </TableCell>
                  <TableCell>
                    <IconButton 
                      onClick={() => handleOpenDialog(task)}
                      disabled={loading}
                    >
                      <EditIcon sx={{ color: 'primary.main' }}/>
                    </IconButton>
                    <IconButton 
                      onClick={() => handleDelete(task.taskId)}
                      disabled={loading}
                    >
                      <DeleteIcon  sx={{ color: 'error.main' }}/>
                    </IconButton>
                  </TableCell>
                  
                </TableRow>
              ))}


              
            </TableBody>
          </Table>
        </TableContainer>
      )}

 

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>{isEdit ? 'Edit Task' : 'Add Task'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="title"
            label="Title"
            type="text"
            fullWidth
            variant="standard"
            defaultValue={currentTaskRef.current.title}
            onChange={handleInputChange}
          />
          <TextField
            margin="dense"
            name="description"
            label="Description"
            type="text"
            fullWidth
            variant="standard"
            multiline
            rows={4}
            defaultValue={currentTaskRef.current.description}
            onChange={handleInputChange}
          />
          <FormControl fullWidth margin="dense" variant="standard">
            <InputLabel>Status</InputLabel>
            <Select
              name="completed"
              defaultValue={currentTaskRef.current.completed ? 'true' : 'false'}
              onChange={handleInputChange}
            >
              <MenuItem value="false">Pending</MenuItem>
              <MenuItem value="true">Completed</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <CustomButton onClick={() => setOpenDialog(false)} disabled={loading}>
            Cancel
          </CustomButton>
          <CustomButton onClick={handleSubmit} disabled={loading}>
            {loading ? <CircularProgress size={24} /> : isEdit ? 'Update' : 'Create'}
          </CustomButton>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default TasksPage;