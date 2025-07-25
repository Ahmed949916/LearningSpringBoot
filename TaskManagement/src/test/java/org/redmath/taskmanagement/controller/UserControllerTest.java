package org.redmath.taskmanagement.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @Test
    public void testGetAllUsersWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetUserByIdWithoutAuth() throws Exception {
       mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("{noop}test"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("ROLE_USER"));
    }


    public Long createUserWithoutAuth(String userName,String pass,String role ) throws Exception {
        Users user=new Users();
        user.setUsername(userName);
        user.setPassword(pass);
        user.setRole(role);
        String newUserJson = objectMapper.writeValueAsString(user);

        MvcResult result=mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        return node.get("userId").asLong();
    }

    @Test
    public void testCreateUserWithoutAuth() throws Exception{
        Long id = createUserWithoutAuth("testCreate", "testCreatePass","ROLE_USER");
        Users savedUser = userRepo.findById(id).orElseThrow();
        assertEquals("testCreate", savedUser.getUsername());
        assertEquals("testCreatePass", savedUser.getPassword());

    }

    @Test
    public void testDeleteUserWithoutAuthWithUserRole() throws Exception {
        Long id = createUserWithoutAuth("testDelete", "testDeletePass","ROLE_USER");
        mockMvc.perform(delete("/api/user/" + id))
                .andExpect(status().isForbidden());


    }
    @Test
    public void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/user/9999"))
                .andExpect(status().isNotFound());
    }













}