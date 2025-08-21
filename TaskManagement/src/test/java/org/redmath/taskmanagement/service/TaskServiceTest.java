package org.redmath.taskmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.dto.TaskCreateDto;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.TaskRepo;
import org.redmath.taskmanagement.repository.UserRepo;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepo taskRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private TaskService taskService;

    private Task testTask;
    private TaskCreateDto createRequest;

    @BeforeEach
    public void setup() {
        testTask = new Task();
        testTask.setTaskId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setOwnerId(1L);

        createRequest = new TaskCreateDto();
        createRequest.setTitle("New Task");
        createRequest.setDescription("New Description");
    }


    @Test
    public void testGetTasksByUserId() throws Exception {
        Task task2 = new Task();
        task2.setTaskId(2L);
        task2.setTitle("Second Task");
        task2.setOwnerId(1L);
        List<Task> taskList = Arrays.asList(testTask, task2);
        when(taskRepo.findByOwnerId(1L)).thenReturn(taskList);
        List<Task> result = taskService.getTasksByUserId(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        assertEquals("Second Task", result.get(1).getTitle());
        verify(taskRepo, times(1)).findByOwnerId(1L);
    }


    @Test
    public void testGetTaskById_NotFound()  {
        when(taskRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.findById(99L, 1L));
        verify(taskRepo, times(1)).findById(99L);
    }


    @Test
    public void testUpdateTask_NotFound()   {
        when(taskRepo.findById(99L)).thenReturn(Optional.empty());
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(99L, updatedTask, 1L));
        verify(taskRepo, times(1)).findById(99L);
        verify(taskRepo, never()).save(any(Task.class));
    }


    @Test
    public void testDeleteTask_NotFound()   {
        when(taskRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.deleteTask(99L, 1L));
        verify(taskRepo, times(1)).findById(99L);
        verify(taskRepo, never()).delete(any(Task.class));
    }

    @Test
    public void testCreateTask()  {

        Users owner = new Users();
        owner.setUserId(1L);
        owner.setUsername("owner@example.com");

        when(userRepo.findById(1L)).thenReturn(Optional.of(owner));


        when(taskRepo.save(any(Task.class))).thenAnswer(invocation -> {
            Task taskToSave = invocation.getArgument(0);
            taskToSave.setTaskId(1L);
            return taskToSave;
        });


        Task createdTask = taskService.createTask(createRequest, 1L);


        assertNotNull(createdTask);
        assertEquals(1L, createdTask.getTaskId());
        assertEquals("New Task", createdTask.getTitle());
        assertEquals("New Description", createdTask.getDescription());
        assertEquals(1L, createdTask.getOwnerId());

        verify(userRepo, times(1)).findById(1L);
        verify(taskRepo, times(1)).save(any(Task.class));


        verifyNoMoreInteractions(taskRepo, userRepo);
    }
    @Test
    public void testCreateTask_WithCompletedValue() {

        Users owner = new Users();
        owner.setUserId(1L);
        owner.setUsername("owner@example.com");
        when(userRepo.findById(1L)).thenReturn(Optional.of(owner));


        TaskCreateDto request = new TaskCreateDto();
        request.setTitle("Task with completed");
        request.setDescription("Testing completed flag");
        request.setCompleted(true);


        when(taskRepo.save(any(Task.class))).thenAnswer(invocation -> {
            Task taskToSave = invocation.getArgument(0);
            taskToSave.setTaskId(1L);
            return taskToSave;
        });


        Task createdTask = taskService.createTask(request, 1L);


        assertTrue(createdTask.getCompleted(), "Task should be marked as completed");
    }

    @Test
    public void testCreateTask_WithNullCompleted() {

        Users owner = new Users();
        owner.setUserId(1L);
        owner.setUsername("owner@example.com");
        when(userRepo.findById(1L)).thenReturn(Optional.of(owner));


        TaskCreateDto request = new TaskCreateDto();
        request.setTitle("Task with null completed");
        request.setDescription("Testing default completed flag");
        request.setCompleted(null);


        when(taskRepo.save(any(Task.class))).thenAnswer(invocation -> {
            Task taskToSave = invocation.getArgument(0);
            taskToSave.setTaskId(1L);
            return taskToSave;
        });


        Task createdTask = taskService.createTask(request, 1L);


        assertFalse(createdTask.getCompleted(), "Task should default to not completed");
    }
    @Test
    public void testCreateTaskUSerNotFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.createTask(createRequest, 1L));
        verify(userRepo, times(1)).findById(1L);
        verify(taskRepo, never()).save(any(Task.class));
    }
    @Test
    public void testUpdateTaskUserNotFound() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");


        when(taskRepo.findById(1L)).thenReturn(Optional.of(testTask));
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(1L, updatedTask, 1L));
        verify(userRepo, times(1)).findById(1L);
        verify(taskRepo, times(1)).findById(1L);
        verify(taskRepo, never()).save(any(Task.class));
    }

    @Test
    public void testDeleteTaskUserNotFound() {

        when(taskRepo.findById(1L)).thenReturn(Optional.of(testTask));
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.deleteTask(1L, 1L));
        verify(userRepo, times(1)).findById(1L);
        verify(taskRepo, times(1)).findById(1L);
        verify(taskRepo, never()).delete(any(Task.class));
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        Users owner = new Users();
        owner.setUserId(1L);
        owner.setUsername("owner@example.com");

        when(userRepo.findById(1L)).thenReturn(Optional.of(owner));
        when(taskRepo.findById(1L)).thenReturn(Optional.of(testTask));

        taskService.deleteTask(1L, 1L);

        verify(taskRepo, times(1)).findById(1L);
        verify(taskRepo, times(1)).delete(testTask);
    }

    @Test
    public void updateTask_OtherUser()   {
        Users owner = new Users();
        owner.setUserId(2L);
        owner.setUsername("owner@example.com");

        when(userRepo.findById(2L)).thenReturn(Optional.of(owner));
        when(taskRepo.findById(1L)).thenReturn(Optional.of(testTask));

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");

        Exception exception = assertThrows(java.nio.file.AccessDeniedException.class, () -> taskService.updateTask(1L, updatedTask, 2L));
        assertEquals("You can only update your own tasks", exception.getMessage());

    }

    @Test
    public void deleteTask_OtherUser()  {
        Users owner = new Users();
        owner.setUserId(2L);
        owner.setUsername("owner@example.com");
        when(userRepo.findById(2L)).thenReturn(Optional.of(owner));

        when(taskRepo.findById(1L)).thenReturn(Optional.of(testTask));

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");

        Exception exception = assertThrows(java.nio.file.AccessDeniedException.class, () -> taskService.deleteTask(1L, 2L));
        assertEquals("You can only delete your own tasks", exception.getMessage());

    }
    @Test
    public void testUpdateExistingTaskAsRequested() {

        Task existingTask = new Task();
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setCompleted(false);


        Task fullUpdateRequest = new Task();
        fullUpdateRequest.setTitle("New Title");
        fullUpdateRequest.setDescription("New Description");
        fullUpdateRequest.setCompleted(true);

        taskService.updateExistingTaskAsRequested(existingTask, fullUpdateRequest);

        assertEquals("New Title", existingTask.getTitle());
        assertEquals("New Description", existingTask.getDescription());
        assertTrue(existingTask.getCompleted());


        existingTask = new Task();
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setCompleted(false);

        Task partialUpdateRequest = new Task();
        partialUpdateRequest.setTitle("Updated Title");

        partialUpdateRequest.setCompleted(true);

        taskService.updateExistingTaskAsRequested(existingTask, partialUpdateRequest);

        assertEquals("Updated Title", existingTask.getTitle());
        assertEquals("Original Description", existingTask.getDescription());
        assertTrue(existingTask.getCompleted());


        existingTask = new Task();
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setCompleted(false);

        Task emptyUpdateRequest = new Task();


        taskService.updateExistingTaskAsRequested(existingTask, emptyUpdateRequest);

        assertEquals("Original Title", existingTask.getTitle());
        assertEquals("Original Description", existingTask.getDescription());
        assertFalse(existingTask.getCompleted());
    }

}