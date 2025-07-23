package org.redmath.taskmanagement.controller;

import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    private UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public Users getUserById(@PathVariable  Long id){
        return userService.getUserById(id);
    }
}
