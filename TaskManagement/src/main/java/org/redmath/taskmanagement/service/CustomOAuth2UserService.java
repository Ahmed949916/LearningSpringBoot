package org.redmath.taskmanagement.service;

import org.redmath.taskmanagement.entity.*;
import org.redmath.taskmanagement.repository.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomOAuth2UserService extends  DefaultOAuth2UserService {
    private final UserRepo userRepo;
    public CustomOAuth2UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
        Users user = userRepo.findByUsername(email)
                .orElseGet(() -> {
                    System.out.println(" Saving new user: " + email);
                    Users newUser = new Users();
                    newUser.setUsername(email);
                    newUser.setPassword("");
                    newUser.setRole("ROLE_USER");
                    return userRepo.save(newUser);
                });
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                oAuth2User.getAttributes(),
                "email"
        );
    }
}
