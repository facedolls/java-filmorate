package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import javax.validation.constraints.*;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre getGenreById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getGenreById(id);
    }
}