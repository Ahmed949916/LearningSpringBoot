package  redmath.news;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.redmath.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@AutoConfigureMockMvc
@SpringBootTest(classes = Main.class)
public class NewApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetById () throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/news/123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(123)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("title 123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("details 123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.is("reporter 123")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportedAt", Matchers.notNullValue()));

    }
}
