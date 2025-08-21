package org.redmath.taskmanagement.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.redmath.taskmanagement.security.WithMockJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;


    @WithMockJwt
    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("ahmed40152@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @WithMockJwt
    public Long createUser(String userName, String pass, String role) throws Exception {
        Users user = new Users();
        user.setUsername(userName);
        user.setPassword(pass);
        user.setRole(role);
        String newUserJson = objectMapper.writeValueAsString(user);

        MvcResult result = mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        return node.get("userId").asLong();
    }

    @WithMockJwt
    @Test
    public void testCreateUser() throws Exception {
        Long id = createUser("testCreate", "testCreatePass", "ROLE_USER");
        Users savedUser = userRepo.findById(id).orElseThrow();
        assertEquals("testCreate", savedUser.getUsername());
        assertEquals("testCreatePass", savedUser.getPassword());
    }

    @WithMockJwt(roles = {"USER"})
    @Test
    public void testDeleteUserWithUserRole() throws Exception {
        Long id = createUser("testDeleteForbidden", "testDeletePass", "ROLE_USER");
        mockMvc.perform(delete("/api/user/" + id))
                .andExpect(status().isForbidden());
    }

    @WithMockJwt(roles = {"ADMIN"})
    @Test
    public void testDeleteUserWithAdminRole() throws Exception {
        Long id = createUser("testDeleteSuccess", "testDeletePass", "ROLE_USER");
        mockMvc.perform(delete("/api/user/" + id))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/api/user/" + id))
                .andExpect(status().isNotFound());
    }

    @WithMockJwt
    @Test
    public void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/9999"))
                .andExpect(status().isNotFound());
    }

}