package org.redmath.taskmanagement.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllUsersWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk());
    }

    // You can still test with authentication if needed
    @Test
    public void testGetUserByIdWithMockAuth() throws Exception {
        mockMvc.perform(get("/api/user/1")
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk());
    }
}