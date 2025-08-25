package org.redmath.taskmanagement.controller;
import lombok.extern.slf4j.Slf4j;
import org.redmath.taskmanagement.dto.UserProfileDto;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.service.UserService;
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


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }



    @GetMapping("/profile")
    public UserProfileDto getCurrentUser(@AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {
        return userService.getProfileFromJwt(jwt);
    }

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable  Long id){
        return userService.getUserById(id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) throws AccessDeniedException {

        userService.deleteUser(id);
    }
}
