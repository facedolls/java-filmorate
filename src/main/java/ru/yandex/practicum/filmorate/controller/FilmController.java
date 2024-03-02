package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10") @Positive Integer count) {
        return filmService.getPopularFilm(count);
    }

    @GetMapping("/genres")
    @ResponseStatus(HttpStatus.OK)
    public Collection<String> getAllGenres() {
        return filmService.getAllGenres();
    }
    @GetMapping("/genres/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getGenreById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    @ResponseStatus(HttpStatus.OK)
    public Collection<String> getAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String getMpaById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getMpaById(id);
    }

    @PostMapping("/films")
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film putLike(@PathVariable @NotNull @Min(1) Integer id, @PathVariable @NotNull @Min(1) Long userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Film deleteLike(@PathVariable @NotNull @Min(1) Integer id, @PathVariable @NotNull @Min(1) Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/films/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteFilm(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.deleteFilm(id);
    }
}