package org.redmath.taskmanagement.controller;


import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.entity.TaskCreateRequest;
import org.redmath.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody TaskCreateRequest task, @AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("userId");
        return taskService.createTask(task, userId);
    }

    @PatchMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task, @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        Long userId = jwt.getClaim("userId");
        return taskService.updateTask(id, task, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        Long userId = jwt.getClaim("userId");
        taskService.deleteTask(id, userId);
    }

    @GetMapping
    public List<Task> getTasksByUserId(@AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        Long userId = jwt.getClaim("userId");
        String username = jwt.getClaim("username");
        return taskService.getTasksByUserId(userId);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        Long userId = jwt.getClaim("userId");
        return taskService.findById(id, userId);
    }


}