import { useState, useEffect } from 'react';
 
import { 
  Box, Button, Typography, Table, TableBody, TableCell, 
  TableContainer, TableHead, TableRow, Paper, IconButton,
  Dialog, DialogTitle, DialogContent, DialogActions, TextField,
  CircularProgress, Alert, Snackbar
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
  const [currentTask, setCurrentTask] = useState({ title: '', description: '' });
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
      setCurrentTask({ title: '', description: '' });
      setIsEdit(false);
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCurrentTask(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      if (isEdit) {
        await updateTask(currentTask.taskId, currentTask);
        setSuccess('Task updated successfully');
      } else {
        await createTask(currentTask);
        setSuccess('Task created successfully');
      }
      fetchTasks();
      handleCloseDialog();
    } catch (error) {
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
console.log("ewrwerwerrewrewr",tasks);
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