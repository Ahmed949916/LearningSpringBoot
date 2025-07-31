import { useState, useEffect } from 'react';
 
import { 
  Box, Button, Typography, Table, TableBody, TableCell, 
  TableContainer, TableHead, TableRow, Paper, IconButton,
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
  const { token, logout } = useAuth();
  const [tasks, setTasks] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [currentTask, setCurrentTask] = useState({ title: '', description: '', completed: false });
  const [isEdit, setIsEdit] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

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
    if (task) {
      setCurrentTask(task);
      setIsEdit(true);
    } else {
      setCurrentTask({ title: '', description: '', completed: false });
      setIsEdit(false);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    console.log('Input change:', name, value);
    if (name === 'completed') {
      const booleanValue = value === 'true';
      console.log('Converting completed value:', value, '→', booleanValue);
      setCurrentTask(prev => {
        const updated = { ...prev, [name]: booleanValue };
        console.log('Updated task state:', updated);
        return updated;
      });
    } else {
      setCurrentTask(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      console.log('Submitting task:', currentTask);
      if (isEdit) {
        console.log('Updating task with data:', currentTask);
        await updateTask(currentTask.taskId, currentTask);
        setSuccess('Task updated successfully');
      } else {
        console.log('Creating task with data:', currentTask);
        await createTask(currentTask);
        setSuccess('Task created successfully');
      }
      fetchTasks();
      handleCloseDialog();
    } catch (error) {
      console.error('Submit error:', error);
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
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow  sx={{ backgroundColor: '#fffcf4ff' }}>
                <TableCell>Title</TableCell>
                <TableCell>Description</TableCell>
                <TableCell>Actions</TableCell>
                <TableCell>Status</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {tasks.map((task) => (
                <TableRow key={task.taskId}>
                  <TableCell>{task.title}</TableCell>
                  <TableCell>{task.description}</TableCell>
                  <TableCell>
                    <IconButton 
                      onClick={() => handleOpenDialog(task)}
                      disabled={loading}
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton 
                      onClick={() => handleDelete(task.taskId)}
                      disabled={loading}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                  <TableCell>
                    {task.completed ? 'Completed' : 'Pending'}
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

 

      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle sx={{  color:"#4A9782" }}>{isEdit ? 'Edit Task' : 'Add Task'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="title"
            label="Title"
            type="text"
            fullWidth
          
            variant="standard"
            value={currentTask.title}
            onChange={handleInputChange}
            disabled={loading}
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
            value={currentTask.description}
            onChange={handleInputChange}
            disabled={loading}
          />
          <FormControl fullWidth margin="dense" variant="standard">
            <InputLabel id="status-label">Status</InputLabel>
            <Select
              labelId="status-label"
              name="completed"
              value={currentTask.completed ? 'true' : 'false'}
              onChange={handleInputChange}
              disabled={loading}
            >
              <MenuItem value="false">Pending</MenuItem>
              <MenuItem value="true">Completed</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <CustomButton onClick={handleCloseDialog} disabled={loading}>
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