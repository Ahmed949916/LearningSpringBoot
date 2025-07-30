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
const dummyTask=[
  {
    "taskId": 1,
    "title": "Finish React project",
    "description": "Complete all components and connect to backend",
    "ownerId": 101
  },
  {
    "taskId": 2,
    "title": "Write blog post",
    "description": "Write a new post about React hooks and context",
    "ownerId": 101
  },
  {
    "taskId": 3,
    "title": "Team meeting",
    "description": "Discuss progress and blockers with the development team",
    "ownerId": 101
  },
  {
    "taskId": 4,
    "title": "Review PRs",
    "description": "Go through all pending pull requests before EOD",
    "ownerId": 101
  },
  {
    "taskId": 5,
    "title": "Update documentation",
    "description": "Add new API endpoints to the Postman collection and README",
    "ownerId": 101
  }
]

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
        <Button 
          variant="contained" 
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
          disabled={loading}
        >
          Add Task
        </Button>
      </Box>

      {loading ? (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
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
          <Button onClick={handleCloseDialog} disabled={loading}>Cancel</Button>
          <Button onClick={handleSubmit} disabled={loading}>
            {loading ? <CircularProgress size={24} /> : isEdit ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default TasksPage;