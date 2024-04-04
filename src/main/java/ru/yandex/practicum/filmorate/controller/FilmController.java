package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@RequestParam(defaultValue = "10") @Positive Integer count,
                                           @RequestParam(defaultValue = "0", required = false) Integer genreId,
                                           @RequestParam(defaultValue = "0", required = false) Integer year) {
        return filmService.getPopularFilm(count, genreId, year);
    }


    @GetMapping("/director/{directorId}")
    public Collection<Film> getFilmsByDirector(@PathVariable @NotNull @Min(1) Integer directorId,
                                                @RequestParam @NotBlank String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        if (film.getId() != 0) {
            log.warn("Incorrect id={} was passed when creating the film: ", film.getId());
            throw new ValidationException("id for the film must not be specified");
        }
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable @NotNull @Min(1) Integer id, @PathVariable @NotNull @Min(1) Long userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable @NotNull @Min(1) Integer id, @PathVariable @NotNull @Min(1) Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public String deleteFilm(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.deleteFilm(id);
    }

    @GetMapping("/search")
    public Collection<Film> searchFilms(@RequestParam String query, @RequestParam String by) {
        return filmService.searchFilms(query, by);
    }
}