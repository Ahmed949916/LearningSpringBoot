package org.redmath.taskmanagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.entity.Users;
import org.redmath.taskmanagement.repository.TaskRepo;
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
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepo taskRepo;


    @Test
    public void testGetTaskByIdWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/task/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("First Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This is a sample task for the test user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(1));
    }

    public Long createTaskWithoutAuth() throws Exception {
        Task task=new Task();
        task.setTitle("new task");
        task.setDescription("description");
        task.setOwnerId(1L);
        String newUserJson = objectMapper.writeValueAsString(task);

        MvcResult result=mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        return node.get("taskId").asLong();
    }

    @Test
    public void testCreateTaskWithoutAuth() throws Exception{
        Long id = createTaskWithoutAuth();
        Task task = taskRepo.findById(id).orElseThrow();
        assertEquals("new task", task.getTitle());
        assertEquals("description", task.getDescription());
        assertEquals(1L,task.getOwnerId());

    }

    @Test
    public void testDeleteTaskWithoutAuth() throws Exception {
        Long id = createTaskWithoutAuth();
        mockMvc.perform(delete("/api/task/" + id))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/api/task/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTaskWithoutAuth() throws Exception {
        Long id = createTaskWithoutAuth();
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setOwnerId(1L);

        String updatedTaskJson = objectMapper.writeValueAsString(updatedTask);

        mockMvc.perform(patch("/api/task/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskId").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(1));
    }

    @Test
    public void testGetTasksByUserIdWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/task/owner/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("First Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("This is a sample task for the test user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ownerId").value(1));
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/task/9999"))
                .andExpect(status().isNotFound());
    }




}