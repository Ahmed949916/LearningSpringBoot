import { useEffect, useRef } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, MenuItem, CircularProgress } from '@mui/material';
import CustomButton from '../CustomButton';
import CustomInput from '../CustomInput';
import CustomSelect from '../CustomSelect';

const TaskDialog = ({ open, onClose, initialTask, onSave, loading }) => {
  const titleRef = useRef(null);
  const descRef = useRef(null);
  const statusRef = useRef(null);

  useEffect(() => {
    if (open) setTimeout(() => titleRef.current?.focus(), 0);
  }, [open]);

  const handleSubmit = () => {
    const title = titleRef.current?.value?.trim() || '';
    const description = descRef.current?.value?.trim() || '';
    const completed = (statusRef.current?.value || 'false') === 'true';
    if (!title || !description) { alert('Please fill in all fields'); return; }
    onSave({ title, description, completed });
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle sx={{fontWeight:"bold",textAlign:"center"}}>
        {initialTask ? 'Edit Task' : 'Add Task'}
      </DialogTitle>
      <DialogContent sx={{ minWidth: 400 }}>
        <CustomInput
          label="Title"
          name="title"
          defaultValue={initialTask?.title ?? ''}
          inputRef={titleRef}
          required
          sx={{ mt: 1 }}
        />
        <CustomInput
          label="Description"
          name="description"
          defaultValue={initialTask?.description ?? ''}
          inputRef={descRef}
          multiline
          rows={4}
          required
        />
        <CustomSelect
          label="Status"
          name="completed"
          defaultValue={initialTask?.completed ? 'true' : 'false'}
          inputRef={statusRef}
          fullWidth
        >
          <MenuItem value="false">Pending</MenuItem>
          <MenuItem value="true">Completed</MenuItem>
        </CustomSelect>
      </DialogContent>
      <DialogActions sx={{ p: 2 }}>
        <CustomButton sx={{ backgroundColor: 'primary.textLight', color: 'primary.lightMain' }} onClick={onClose} disabled={loading}>
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