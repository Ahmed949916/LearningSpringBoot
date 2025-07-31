package org.redmath.taskmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redmath.taskmanagement.entity.Users;

import org.redmath.taskmanagement.repository.UserRepo;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    private Users testUser;

    @BeforeEach
    public void setup() {
        testUser = new Users();
        testUser.setUserId(1L);
        testUser.setUsername("test@example.com");
        testUser.setPassword("password");
        testUser.setRole("ROLE_USER");
    }

    @Test
    public void testCreateUser() {
        when(userRepo.save(any(Users.class))).thenReturn(testUser);

        Users result = userService.createUser(testUser);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("test@example.com", result.getUsername());
        verify(userRepo, times(1)).save(any(Users.class));
    }

    @Test
    public void testGetAllUsers() {
        Users user2 = new Users();
        user2.setUserId(2L);
        user2.setUsername("user2@example.com");

        List<Users> usersList = Arrays.asList(testUser, user2);
        when(userRepo.findAll()).thenReturn(usersList);

        List<Users> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("test@example.com", result.get(0).getUsername());
        assertEquals("user2@example.com", result.get(1).getUsername());
        verify(userRepo, times(1)).findAll();
    }

    @Test
    public void testGetUserById_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));

        Users result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("test@example.com", result.getUsername());
        verify(userRepo, times(1)).findById(1L);
    }






}