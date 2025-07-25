
package org.redmath.taskmanagement.controller;

import lombok.Generated;
import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.entity.TaskCreateRequest;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


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
    public Task createTask(@RequestBody TaskCreateRequest task, @AuthenticationPrincipal OAuth2User principal) {
        String username = principal.getAttribute("email");
        return taskService.createTask(task, username);
    }

    @PatchMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task, @AuthenticationPrincipal OAuth2User principal) throws AccessDeniedException {
        String username = principal.getAttribute("email");
        return taskService.updateTask(id, task, username);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) throws AccessDeniedException {
        String username = principal.getAttribute("email");
        taskService.deleteTask(id, username);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal)  throws AccessDeniedException {
        String username = principal.getAttribute("email");
        return taskService.findById(id, username);
    }

    @GetMapping("/owner/{id}")
    public List<Task> getTasksByUserId(@PathVariable Long id, @AuthenticationPrincipal OAuth2User principal) throws AccessDeniedException {
        String username = principal.getAttribute("email");


        return taskService.getTasksByUserId(id,username);
    }
}