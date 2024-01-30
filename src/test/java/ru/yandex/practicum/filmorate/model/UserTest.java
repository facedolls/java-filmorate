package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import java.time.LocalDate;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

        /*@Test
        public void validation() throws Exception {
            User user = new User("mama@yandex.ru", "asfghjk", null,
                    LocalDate.of(2000, 12, 15));
            mockMvc.perform(post("/users")
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON))

                    .andExpect(jsonPath("$.email").value("mama@yandex.ru"))
                    .andExpect(jsonPath("$.login").value("asfghjk"))
                    .andExpect(jsonPath("$.name").value("asfghjk"))
                    .andExpect(jsonPath("$.birthday").value("2000-12-15"))
                    .andExpect(status().is(200));
        }*/
}