package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepo userRepo;

    public CustomOAuth2UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // Get user info from Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract Google email
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Check if user exists in DB
        Users user = userRepo.findByUsername(email)
                .orElseGet(() -> {
                    // If not exists, create a new one
                    Users newUser = new Users();
                    newUser.setUsername(email);
                    newUser.setPassword(""); // no password needed for Google login
                    newUser.setRole("ROLE_USER");
                    return userRepo.save(newUser);
                });

        // Return Spring Security User with ROLE_USER
        return new DefaultOAuth2User(
                Collections.singleton(() -> user.getRole()), // roles
                oAuth2User.getAttributes(),
                "email" // key to use as principal name
        );
    }
}
