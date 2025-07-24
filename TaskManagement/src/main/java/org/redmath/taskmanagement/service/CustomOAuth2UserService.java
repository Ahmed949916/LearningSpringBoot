package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // ✅ Load user info from Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // ✅ Extract email
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        System.out.println("✅ Google attributes: " + oAuth2User.getAttributes());

        // ✅ Fetch existing user or create new one
        Users user = userRepo.findByUsername(email)
                .orElseGet(() -> {
                    System.out.println("ℹ️ Saving new user: " + email);
                    Users newUser = new Users();
                    newUser.setUsername(email);
                    newUser.setPassword(""); // no password for OAuth users
                    newUser.setRole("ROLE_USER");
                    return userRepo.save(newUser);
                });

        // ✅ Return Spring Security OAuth2 user with correct authority
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                oAuth2User.getAttributes(),
                "email"  // principal name key
        );
    }
}
