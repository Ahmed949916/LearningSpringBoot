package org.redmath.taskmanagement.controller;

import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }


    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public Users getCurrentUser(@AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        String userId= jwt.getClaim("userId");
        if (userId == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return userService.getUserById(Long.parseLong(userId));
    }


    @GetMapping("/{id}")
    public Users getUserById(@PathVariable  Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);


    }
}
