package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.validator.count.CorrectFilmCount;
import ru.yandex.practicum.filmorate.validator.id.film.CorrectFilmId;
import ru.yandex.practicum.filmorate.validator.id.user.CorrectUserId;
import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable @CorrectFilmId Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10") @CorrectFilmCount Integer count) {
        return filmService.getPopularFilm(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film putLike(@PathVariable @CorrectFilmId Integer id, @PathVariable @CorrectUserId Long userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film deleteLike(@PathVariable @CorrectFilmId Integer id, @PathVariable @CorrectUserId Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteFilm(@PathVariable @CorrectFilmId Integer id) {
        return filmService.deleteFilm(id);
    }
}