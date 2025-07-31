// java
package org.redmath.taskmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.entity.TaskCreateRequest;
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
    private TaskCreateRequest createRequest;

    @BeforeEach
    public void setup() {
        testTask = new Task();
        testTask.setTaskId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setOwnerId(1L);

        createRequest = new TaskCreateRequest();
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
    public void testGetTaskById_NotFound() throws Exception {
        when(taskRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.findById(99L, 1L));
        verify(taskRepo, times(1)).findById(99L);
    }


    @Test
    public void testUpdateTask_NotFound() throws Exception {
        when(taskRepo.findById(99L)).thenReturn(Optional.empty());
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");
        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(99L, updatedTask, 1L));
        verify(taskRepo, times(1)).findById(99L);
        verify(taskRepo, never()).save(any(Task.class));
    }




    @Test
    public void testDeleteTask_NotFound() throws Exception {
        when(taskRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> taskService.deleteTask(99L, 1L));
        verify(taskRepo, times(1)).findById(99L);
        verify(taskRepo, never()).delete(any(Task.class));
    }

    @Test
    public void testCreateTask() throws Exception {

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


}