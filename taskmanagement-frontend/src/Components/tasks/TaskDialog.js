import { useEffect, useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, FormControl, InputLabel, Select, MenuItem, CircularProgress } from '@mui/material';
import CustomButton from '../../Components/CustomButton';

const emptyTask = { title: '', description: '', completed: false };

const TaskDialog = ({ open, onClose, initialTask, onSave, loading }) => {
  const [form, setForm] = useState(emptyTask);

  useEffect(() => {
    if (open) {
      setForm(initialTask ? {
        title: initialTask.title || '',
        description: initialTask.description || '',
        completed: !!initialTask.completed
      } : emptyTask);
    }
  }, [open, initialTask]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'completed') {
      setForm((f) => ({ ...f, completed: value === 'true' }));
    } else {
      setForm((f) => ({ ...f, [name]: value }));
    }
  };

  const handleSubmit = () => {
    onSave(form);
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>{initialTask ? 'Edit Task' : 'Add Task'}</DialogTitle>
      <DialogContent>
        <TextField
          autoFocus
          margin="dense"
          name="title"
          label="Title"
          type="text"
          fullWidth
          variant="standard"
          value={form.title}
          onChange={handleChange}
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
          value={form.description}
          onChange={handleChange}
        />
        <FormControl fullWidth margin="dense" variant="standard">
          <InputLabel>Status</InputLabel>
          <Select
            name="completed"
            value={form.completed ? 'true' : 'false'}
            onChange={handleChange}
            label="Status"
          >
            <MenuItem value="false">Pending</MenuItem>
            <MenuItem value="true">Completed</MenuItem>
          </Select>
        </FormControl>
      </DialogContent>
      <DialogActions>
        <CustomButton sx={{ backgroundColor: '#fff', border: '1px solid #ccc', color: '#4A9782' }} onClick={onClose} disabled={loading}>
          Cancel
        </CustomButton>
        <CustomButton onClick={handleSubmit} disabled={loading}>
          {loading ? <CircularProgress size={24} /> : initialTask ? 'Update' : 'Create'}
        </CustomButton>
      </DialogActions>
    </Dialog>
  );
};

export default TaskDialog;
