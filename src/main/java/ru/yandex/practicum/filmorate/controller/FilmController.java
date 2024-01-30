package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
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
        if (film.getId() != null) {
            log.warn("An exception \"FilmAlreadyExistException\" was thrown for {}: film already exist", film);
            throw new FilmAlreadyExistException("The film already exist: " + film);
        }
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Create film: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Update film: {}", film);
        } else {
            log.warn("An exception \"ValidationException\" was thrown for {}: incorrect id passed", film);
            throw new ValidationException("Incorrect id passed for: " + film);
        }
        return film;
    }
}