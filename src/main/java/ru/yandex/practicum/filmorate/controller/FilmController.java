package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable @NotNull @Min(1L) Long id) {
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

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public Collection<Film> getFilmsByDirector(@PathVariable @NotNull @Min(1) Integer directorId,
                                               @RequestParam @NotBlank String sortBy) {
        return filmService.getFilmsByDirector(directorId, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}/{grade}")
    public Film putLike(@PathVariable @NotNull @Min(1L) Long id, @PathVariable @NotNull @Min(1L) Long userId,
                        @PathVariable @NotNull @Min(1) @Max(10) Integer grade) {
        return filmService.putLike(id, userId, grade);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable @NotNull @Min(1L) Long id, @PathVariable @NotNull @Min(1L) Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public String deleteFilm(@PathVariable @NotNull @Min(1L) Long id) {
        return filmService.deleteFilm(id);
    }

    @GetMapping("/search")
    public Collection<Film> searchFilms(@RequestParam String query, @RequestParam String by) {
        return filmService.searchFilms(query, by);
    }
}