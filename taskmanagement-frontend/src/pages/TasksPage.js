import { useState, useEffect } from 'react';
import { Box, Typography, CircularProgress, Snackbar, Alert } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { getTasks, createTask, updateTask, deleteTask } from '../services/api';
import { useAuth } from '../Context/AuthContext';
import CustomButton from '../Components/CustomButton';
import CreateTaskDialog from '../Components/tasks/CreateTaskDialog';
import TasksTable from '../Components/tasks/TasksTable';

const TasksPage = () => {
  const { token } = useAuth();
  const [tasks, setTasks] = useState([]);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingTask, setEditingTask] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    if (token) fetchTasks();
  }, [token]);

  const fetchTasks = async () => {
    setLoading(true);
    try {
      const data = await getTasks();
      setTasks(data || []);
    } catch {
      setError('Failed to fetch tasks');
    } finally {
      setLoading(false);
    }
  };

  const handleOpenDialog = (task = null) => {
    setEditingTask(task);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingTask(null);
  };

  const handleSaveTask = async (payload) => {
    setLoading(true);
    try {
      if (editingTask?.taskId) {
        await updateTask(editingTask.taskId, payload);
        setSuccess('Task updated successfully');
      } else {
        await createTask(payload);
        setSuccess('Task created successfully');
      }
      await fetchTasks();
      handleCloseDialog();
    } catch {
      setError(editingTask ? 'Failed to update task' : 'Failed to create task');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    setLoading(true);
    try {
      await deleteTask(id);
      setSuccess('Task deleted successfully');
      await fetchTasks();
    } catch {
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
      <Snackbar open={!!error} autoHideDuration={6000} onClose={() => setError('')}>
        <Alert severity="error" onClose={() => setError('')}>{error}</Alert>
      </Snackbar>
      <Snackbar open={!!success} autoHideDuration={6000} onClose={() => setSuccess('')}>
        <Alert severity="success" onClose={() => setSuccess('')}>{success}</Alert>
      </Snackbar>

      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h4">Tasks</Typography>
        <CustomButton onClick={() => handleOpenDialog()} startIcon={<AddIcon />}>
          Add Task
        </CustomButton>
      </Box>

      {loading ? (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      ) : (
        <TasksTable
          tasks={tasks}
          onEdit={handleOpenDialog}
          onDelete={handleDelete}
          disabled={loading}
        />
      )}

      <CreateTaskDialog
        open={openDialog}
        onClose={handleCloseDialog}
        initialTask={editingTask}
        onSave={handleSaveTask}
        loading={loading}
      />
    </Box>
  );
};

export default TasksPage;
