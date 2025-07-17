package org.redmath.news;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.redmath.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(classes = Main.class)
public class NewApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetById () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/news/123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(123)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("title 123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("details 123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("reporter 123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportedAt", Matchers.notNullValue()));

    }

    private Long createTestNews() throws Exception {
        News news = new News();
        news.setTitle("Initial Title");
        news.setContent("Initial Content");
        news.setAuthor("Initial Author");
        news.setReportedAt(LocalDateTime.now());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(news)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(responseBody);
        return node.get("id").asLong();
    }


    @Test
    public void testCreateAndGetById() throws Exception {
        Long createdId = createTestNews();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/news/" + createdId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(createdId.intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Initial Title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Initial Content")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("Initial Author")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportedAt").exists());
    }


    @Test
    public void testGetAllNews() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/news"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE));

    }
    @Test
    public void testCreateAndUpdateNewsPartial() throws Exception {
        Long createdId = createTestNews();
        News partialUpdate = new News();
        partialUpdate.setTitle("Updated Title Only");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/news/"+createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdate)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Updated Title Only")));

    }

    @Test
    public void testDeleteNews_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/news/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }

    @Test
    public void testDeleteNews() throws Exception {

        Long createdId = createTestNews();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/news/"+createdId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }







}
