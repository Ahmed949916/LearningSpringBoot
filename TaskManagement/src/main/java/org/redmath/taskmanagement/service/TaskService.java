package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepo taskRepository;

    @Autowired
    public TaskService(TaskRepo repo) {
        this.taskRepository = repo;
    }

    public List<Task> getTasksByUserId(Long id) {
        return taskRepository.findByOwnerId(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task with ID " + id + " not found"));
        taskRepository.delete(task);
    }


    public Task updateTask(Long id, Task req) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        if (req.getTitle() != null) existingTask.setTitle(req.getTitle());
        if (req.getDescription() != null) existingTask.setDescription(req.getDescription());
        if (req.getOwnerId() != null) existingTask.setOwnerId(req.getOwnerId());

        return taskRepository.save(existingTask);
    }


}
