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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmValidationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @DisplayName("Должен добавить новый фильм")
    @Test
    public void shouldCreateFilm() throws Exception {
        Film film = new Film("8 Mile", "Jimmy Smith, nicknamed \"Rabbit\"",
                LocalDate.of(2002, 11, 6), 110);
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("8 Mile"))
                .andExpect(jsonPath("$.description").value("Jimmy Smith, nicknamed \"Rabbit\""))
                .andExpect(jsonPath("$.releaseDate").value("2002-11-06"))
                .andExpect(jsonPath("$.duration").value("110"))
                .andExpect(jsonPath("$.like").isEmpty())
                .andExpect(status().is(201));
    }

    @DisplayName("Должен вернуть код ошибки 400, имя фильма пустое")
    @Test
    public void shouldReturnAnErrorCode400NameEmpty() throws Exception {
        Film film = new Film("", "future",
                LocalDate.of(2000, 12, 12), 60);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400, имя фильма состоит из одних пробелов")
    @Test
    public void shouldReturnAnErrorCode400NameBlank() throws Exception {
        Film film = new Film("        ", "present",
                LocalDate.of(2000, 11, 11), 50);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400, описание фильма превышает 200 символов")
    @Test
    public void shouldReturnAnErrorCode400DescriptionExceedsMax() throws Exception {
        Film film = new Film("Mean Girls", "Having lost the trust of her parents, lost her " +
                "friends, ignored by classmates and shunned by Aaron, Kady decides to improve, taking all the blame " +
                "for the gossip on herself. But at the Olympics, Kady, whose opponent turns out to be an unattractive" +
                " girl, realizes that even if she criticizes the girl's appearance, it will not save her from defeat." +
                " Kady wins the tournament and returns to school.",
                LocalDate.of(2004, 6, 10), 97);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400, описание фильма пустое")
    @Test
    public void shouldReturnAnErrorCode400DescriptionEmpty() throws Exception {
        Film film = new Film("Spirited Away", "",
                LocalDate.of(2002, 12, 31), 125);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400, описание фильма состоит из пробелов")
    @Test
    public void shouldReturnAnErrorCode400DescriptionBlank() throws Exception {
        Film film = new Film("Back to the Future", "             ",
                LocalDate.of(1985, 7, 3), 108);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400, дата выхода фильма раньше минимально установленного времени")
    @Test
    public void shouldReturnAnErrorCode400DateBeforeMin() throws Exception {
        Film film = new Film("Date", "Date before min",
                LocalDate.of(1880, 6, 6), 60);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @DisplayName("Должен вернуть код ошибки 400, длительность фильма отрицательная")
    @Test
    public void shouldReturnAnErrorCode400DurationNegative() throws Exception {
        Film film = new Film("Duration", "Duration negative",
                LocalDate.of(2000, 7, 7), -100);

        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}