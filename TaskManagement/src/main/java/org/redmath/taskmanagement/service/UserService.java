package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.UserProfileDto;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_PICTURE = "picture";
    private final UserRepo userRepo;


    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public Users getUserById(Long id) {
        return userRepo.findById(id).orElseThrow();
    }

    public Users createUser(Users user) {
        return userRepo.save(user);
    }


    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(Long id) {
        Users user = userRepo.findById(id).orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
        userRepo.delete(user);
    }

    public UserProfileDto getProfileFromJwt(Jwt jwt) throws AccessDeniedException {
        validateJwt(jwt);
        Long userId = extractUserId(jwt);
        Users user = findUserOrThrow(userId);
        String profilePicture = extractProfilePicture(jwt);
        UserProfileDto profile = new UserProfileDto();
        profile.setUser(user);
        profile.setPhotoLink(profilePicture);
        return profile;
    }

    private void validateJwt(Jwt jwt) throws AccessDeniedException {
        if (jwt == null) {

            throw new AccessDeniedException("Authentication required");
        }
    }

    private Long extractUserId(Jwt jwt) throws AccessDeniedException {
        Long userId = jwt.getClaim(CLAIM_USER_ID);
        if (userId == null) {
            throw new AccessDeniedException("Invalid authentication token");
        }
        return userId;
    }

    private Users findUserOrThrow(Long userId) {
        return userRepo.findById(userId).orElseThrow();
    }

    private String extractProfilePicture(Jwt jwt) {
        return jwt.getClaim(CLAIM_PICTURE);
    }
}
