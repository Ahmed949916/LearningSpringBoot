package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
