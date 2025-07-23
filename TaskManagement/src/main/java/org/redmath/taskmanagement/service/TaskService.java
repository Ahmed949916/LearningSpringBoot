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


    public Task updateTask(Task task) {

        Task existingTask = taskRepository.findById(task.getId())
                .orElseThrow(() -> new NoSuchElementException("Task with ID " + task.getId() + " not found"));
        if (task.getTitle() != null && !task.getTitle().isBlank()) {
            existingTask.setTitle(task.getTitle());
        }
        if (task.getDescription() != null && !task.getDescription().isBlank()) {
            existingTask.setDescription(task.getDescription());
        }
        if (task.getOwner() != null) {
            existingTask.setOwner(task.getOwner());
        }
        return taskRepository.save(existingTask);
    }

}
