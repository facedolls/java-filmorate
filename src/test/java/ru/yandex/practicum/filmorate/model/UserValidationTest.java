package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserValidationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @DisplayName("Должен создать пользователя")
    @Test
    public void shouldCreateUser() throws Exception {
        User user = new User(null,"monika@yandex.ru", "jnb6fds", "Monika",
                LocalDate.of(1989, 1, 19));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("monika@yandex.ru"))
                .andExpect(jsonPath("$.login").value("jnb6fds"))
                .andExpect(jsonPath("$.name").value("Monika"))
                .andExpect(jsonPath("$.birthday").value("1989-01-19"))
                .andExpect(status().is(200));
    }

    @DisplayName("Должен изменить пустое имя пользователя на логин")
    @Test
    public void shouldChangeTheEmptyNameToLogin() throws Exception {
        User user = new User(null,"mama@yandex.ru", "a2sfg2hjk", "",
                LocalDate.of(2000, 12, 15));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("mama@yandex.ru"))
                .andExpect(jsonPath("$.login").value("a2sfg2hjk"))
                .andExpect(jsonPath("$.name").value("a2sfg2hjk"))
                .andExpect(jsonPath("$.birthday").value("2000-12-15"))
                .andExpect(status().is(200));
    }

    @DisplayName("Должен изменить состоящее из одних пробелов имя пользователя на логин")
    @Test
    public void shouldChangeTheBlankNameToLogin() throws Exception {
        User user = new User(null,"papa@yandex.ru", "jh9gvc", "         ",
                LocalDate.of(1999, 11, 14));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("papa@yandex.ru"))
                .andExpect(jsonPath("$.login").value("jh9gvc"))
                .andExpect(jsonPath("$.name").value("jh9gvc"))
                .andExpect(jsonPath("$.birthday").value("1999-11-14"))
                .andExpect(status().is(200));
    }

    @DisplayName("Должен изменить null имя пользователя на логин")
    @Test
    public void shouldChangeTheNullNameToLogin() throws Exception {
        User user = new User(null,"lola@yandex.ru", "lmn8bvc", null,
                LocalDate.of(1998, 10, 13));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value("lola@yandex.ru"))
                .andExpect(jsonPath("$.login").value("lmn8bvc"))
                .andExpect(jsonPath("$.name").value("lmn8bvc"))
                .andExpect(jsonPath("$.birthday").value("1998-10-13"))
                .andExpect(status().is(200));
    }

    @DisplayName("Должен вернуть код ошибки 400 наличии в логине пользователя пробела")
    @Test
    public void shouldReturnAnErrorCode400ForALoginContainingASpace() throws Exception {
        User user = new User(null, "a@yandex.ru", "k k", "Anita",
                LocalDate.of(2000, 12, 15));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400 при пустом логине пользователя")
    @Test
    public void shouldReturnAnErrorCode400AnEmptyLogin() throws Exception {
        User user = new User(null,"nana@yandex.ru", "", "Nana",
                LocalDate.of(1996, 8, 11));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400, дата рождения пользователя указана будущим временем")
    @Test
    public void shouldReturnAnErrorCode400ForUserBirthdayTheFuture() throws Exception {
        User user = new User(null,"han@yandex.ru", "asd9ewq", "Han",
                LocalDate.of(500000, 12, 15));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400 для неправильно сформированного адреса электронной почты")
    @Test
    public void shouldReturnAnErrorCode400ForInvalidEmail() throws Exception {
        User user = new User(null,"yandex", "gbn3hjk", "Luna",
                LocalDate.of(1989, 9, 3));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}