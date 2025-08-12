package org.redmath.taskmanagement.controller;
import lombok.extern.slf4j.Slf4j;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.nio.file.AccessDeniedException;
import java.util.List;


@RestController
@RequestMapping("/api/user")
@Slf4j
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

    @GetMapping("/profile")
    public Users getCurrentUser(@AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        Long userId = jwt.getClaim("userId");
        if (userId == null) {
            throw new AccessDeniedException("User not authenticated");
        }
        return userService.getUserById(userId);
    }


    @GetMapping("/{id}")
    public Users getUserById(@PathVariable  Long id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        log.info("User roles: {}", (Object) jwt.getClaim("roles"));
        userService.deleteUser(id);
    }
}
