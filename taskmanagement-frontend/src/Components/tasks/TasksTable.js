import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const TasksTable = ({ tasks, onEdit, onDelete, disabled }) => {
  return (
    <TableContainer>
      <Table>
        <TableHead>
          <TableRow sx={{ backgroundColor: '#fffcf4ff' }}>
            <TableCell sx={{ fontWeight: 'bold' }}>Title</TableCell>
            <TableCell sx={{ fontWeight: 'bold' }}>Description</TableCell>
            <TableCell sx={{ fontWeight: 'bold' }}>Actions</TableCell>
            <TableCell sx={{ fontWeight: 'bold' }}>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {tasks.map((task) => (
            <TableRow key={task.taskId}>
              <TableCell>{task.title}</TableCell>
              <TableCell>{task.description}</TableCell>
              <TableCell>
                <IconButton onClick={() => onEdit(task)} disabled={disabled}>
                  <EditIcon sx={{ color: 'primary.main' }} />
                </IconButton>
                <IconButton onClick={() => onDelete(task.taskId)} disabled={disabled}>
                  <DeleteIcon sx={{ color: 'error.main' }} />
                </IconButton>
              </TableCell>
              <TableCell>{task.completed ? 'Completed' : 'Pending'}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default TasksTable;
