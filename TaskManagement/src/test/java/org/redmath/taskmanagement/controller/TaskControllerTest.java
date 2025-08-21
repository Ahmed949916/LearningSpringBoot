package org.redmath.taskmanagement.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.redmath.taskmanagement.entity.Task;
import org.redmath.taskmanagement.dto.TaskCreateDto;
import org.redmath.taskmanagement.repository.TaskRepo;
import org.redmath.taskmanagement.security.WithMockJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepo taskRepo;

    @WithMockJwt(userId = 1)
    @Test
    void testGetTaskById() throws Exception {
        mockMvc.perform(get("/api/task/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("First Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This is a sample task for the test user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(1));
    }

    @WithMockJwt(userId = 1)
    @Test
    void testCreateTask() throws Exception {
        TaskCreateDto task = new TaskCreateDto();
        task.setTitle("new task");
        task.setDescription("description");

        String taskJson = objectMapper.writeValueAsString(task);

        MvcResult result = mockMvc.perform(post("/api/task")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        Long id = node.get("taskId").asLong();

        Task createdTask = taskRepo.findById(id).orElseThrow();
        assertEquals("new task", createdTask.getTitle());
        assertEquals("description", createdTask.getDescription());
        assertEquals(1L, createdTask.getOwnerId());
    }

    @WithMockJwt(userId = 1)
    @Test
    void testDeleteTask() throws Exception {
        TaskCreateDto task = new TaskCreateDto();
        task.setTitle("Task to delete");
        task.setDescription("Will be deleted");

        String taskJson = objectMapper.writeValueAsString(task);

        MvcResult result = mockMvc.perform(post("/api/task")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        Long id = node.get("taskId").asLong();

        mockMvc.perform(delete("/api/task/" + id).with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/task/" + id))
                .andExpect(status().isNotFound());
    }

    @WithMockJwt(userId = 1)
    @Test
    void testUpdateTask() throws Exception {
        TaskCreateDto createRequest = new TaskCreateDto();
        createRequest.setTitle("Task to update");
        createRequest.setDescription("Will be updated");

        String createJson = objectMapper.writeValueAsString(createRequest);

        MvcResult result = mockMvc.perform(post("/api/task")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        Long id = node.get("taskId").asLong();

        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");

        String updatedTaskJson = objectMapper.writeValueAsString(updatedTask);

        mockMvc.perform(patch("/api/task/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskId").value(id.intValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Updated Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(1));
    }

    @WithMockJwt(userId = 1)
    @Test
    void testGetTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/task/9999"))
                .andExpect(status().isNotFound());
    }

    @WithMockJwt(userId = 1)
    @Test
    void testGetTasksByUserId() throws Exception {
        mockMvc.perform(get("/api/task"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskId").exists());
    }

    @WithMockJwt(userId = 2)
    @Test
    void testAccessDeniedForDifferentUser() throws Exception {
        mockMvc.perform(get("/api/task/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockJwt(userId = 1, roles = {"ADMIN"})
    @Test
    void testAdminTaskCreation() throws Exception {
        TaskCreateDto task = new TaskCreateDto();
        task.setTitle("Admin task");
        task.setDescription("Created by admin");

        String taskJson = objectMapper.writeValueAsString(task);

        MvcResult result = mockMvc.perform(post("/api/task")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        Long id = node.get("taskId").asLong();

        mockMvc.perform(get("/api/task/" + id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Admin task"));
    }
}
