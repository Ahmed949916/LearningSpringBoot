package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.entity.TaskCreateRequest;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.TaskRepo;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class TaskService {

    private final TaskRepo taskRepository;
    private final UserRepo userRepo;

    @Autowired
    public TaskService(TaskRepo repo, UserRepo userRepo) {
        this.taskRepository = repo;
        this.userRepo = userRepo;
    }

    public List<Task> getTasksByUserId(Long userId) throws AccessDeniedException {

        return taskRepository.findByOwnerId(userId);
    }

    public Task createTask(TaskCreateRequest task,Long userId ) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Task newTask=new Task();
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setOwnerId(user.getUserId());
        newTask.setCompleted(task.getCompleted() != null ? task.getCompleted() : Boolean.FALSE);
        return taskRepository.save(newTask);
    }

    public void deleteTask(Long id, Long userId) throws AccessDeniedException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!task.getOwnerId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only delete your own tasks");
        }

        taskRepository.delete(task);
    }

    public Task updateTask(Long id, Task req, Long userId) throws AccessDeniedException {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!existingTask.getOwnerId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only update your own tasks");
        }

        if (req.getTitle() != null) existingTask.setTitle(req.getTitle());
        if (req.getDescription() != null) existingTask.setDescription(req.getDescription());
        if (req.getOwnerId() != null) existingTask.setOwnerId(req.getOwnerId());
        if (req.getCompleted() != null) existingTask.setCompleted(req.getCompleted());

        return taskRepository.save(existingTask);
    }

    public Task findById(Long id, Long userId) throws AccessDeniedException {
        Task task = taskRepository.findById(id).orElseThrow();

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (!task.getOwnerId().equals(user.getUserId())) {
            throw new AccessDeniedException("You can only view your own tasks");
        }

        return task;
    }
}