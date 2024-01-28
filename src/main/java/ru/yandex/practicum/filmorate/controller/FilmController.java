package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.*;

@RestController()
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static int id = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validationFilm(film);
        generateId(film);
        if (!films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Added film: {}", film);
        } else {
            log.warn("An exception \"FilmAlreadyExistException\" was thrown for {}", film);
            throw new FilmAlreadyExistException("The film already exist: " + film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validationFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Update film: {}", film);
        } else {
            log.warn("An exception \"ValidationException\" was thrown for {}", film);
            throw new ValidationException("Incorrect id passed for: " + film);
        }
        return film;
    }

    private void validationFilm(Film film) {
        boolean isNotDate = film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28));
        if (isNotDate) {
            log.warn("An exception \"ValidationException\" was thrown for {}", film);
            throw new ValidationException("The film was not validated: " + film);
        }
    }

    private void generateId(Film film) {
        if (film.getId() == 0) {
            film.setId(++id);
        } else {
            id = film.getId();
        }
    }
}