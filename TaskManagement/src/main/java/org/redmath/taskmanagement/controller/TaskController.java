
package org.redmath.taskmanagement.controller;

import lombok.Generated;
import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping({"/api/task"})
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }




    @GetMapping("/owner/{id}")
    public List<Task> getTasksByUserId(@PathVariable Long id){
        return taskService.getTasksByUserId(id);
    }
}
