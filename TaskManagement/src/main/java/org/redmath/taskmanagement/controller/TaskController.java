
package org.redmath.taskmanagement.controller;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/task"})
public class TaskController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    @GetMapping({"/test"})
    public void test() {
        log.info("Task controller is working");
    }
}
