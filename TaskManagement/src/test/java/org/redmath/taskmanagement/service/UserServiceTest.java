package org.redmath.taskmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redmath.taskmanagement.dto.UserDto;
import org.redmath.taskmanagement.dto.UserProfileDto;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.security.oauth2.jwt.Jwt;

import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private Jwt jwt;

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

        when(userRepo.findAll()).thenReturn(Arrays.asList(testUser, user2));

        List<UserDto> result = userService.getAllUsers();

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

    @Test
    public void testGetUserById_NotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(99L));
        verify(userRepo, times(1)).findById(99L);
        verify(userRepo, never()).delete(any());
    }

    @Test
    public void testDeleteUser() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));

        userService.deleteUser(1L);

        verify(userRepo, times(1)).delete(testUser);
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(99L));
        verify(userRepo, never()).delete(any());
    }

    @Test
    public void testGetProfileFromJwt_Success() throws AccessDeniedException {
        when(jwt.getClaim("userId")).thenReturn(1L);
        when(jwt.getClaim("picture")).thenReturn("profile.jpg");
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));

        UserProfileDto result = userService.getProfileFromJwt(jwt);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertEquals("profile.jpg", result.getPhotoLink());
    }

    @Test
    public void testGetProfileFromJwt_NullJwt() {
        assertThrows(AccessDeniedException.class, () -> userService.getProfileFromJwt(null));
    }

    @Test
    public void testGetProfileFromJwt_MissingUserId() {
        when(jwt.getClaim("userId")).thenReturn(null);

        assertThrows(AccessDeniedException.class, () -> userService.getProfileFromJwt(jwt));
    }

    @Test
    public void testGetProfileFromJwt_UserNotFound() {
        when(jwt.getClaim("userId")).thenReturn(99L);
        when(userRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getProfileFromJwt(jwt));
    }

    @Test
    public void testGetProfileFromJwt_PictureReturnNull() throws AccessDeniedException {

        when(jwt.getClaim("userId")).thenReturn(1L);
        when(jwt.getClaim("picture")).thenReturn(null);
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));

        UserProfileDto result = userService.getProfileFromJwt(jwt);

        assertNotNull(result);
        assertEquals(testUser, result.getUser());
        assertNull(result.getPhotoLink());
    }
}
