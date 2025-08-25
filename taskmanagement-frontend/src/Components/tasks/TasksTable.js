import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

const TasksTable = ({ tasks, onEdit, onDelete, disabled }) => {
  return (
    <TableContainer>
      <Table>
        <TableHead  sx={{}}>
          <TableRow sx={{ backgroundColor: '#004030' }}>
            <TableCell sx={{ fontWeight: 'bold',color:"#fff" }}>Title</TableCell>
            <TableCell sx={{ fontWeight: 'bold',color:"#fff" }}>Description</TableCell>
            <TableCell sx={{ fontWeight: 'bold',color:"#fff" }}>Actions</TableCell>
            <TableCell sx={{ fontWeight: 'bold',color:"#fff" }}>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {tasks.map((task) => (
            <TableRow key={task.taskId}>
              <TableCell>{task.title}</TableCell>
              <TableCell>{task.description}</TableCell>
              <TableCell>{task.completed ? 'Completed' : 'Pending'}</TableCell>
              <TableCell>
                <IconButton onClick={() => onEdit(task)} disabled={disabled}>
                  <EditIcon sx={{ color: 'primary.main' }} />
                </IconButton>
                <IconButton onClick={() => onDelete(task.taskId)} disabled={disabled}>
                  <DeleteIcon sx={{ color: 'error.main' }} />
                </IconButton>
              </TableCell>
              
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default TasksTable;
