package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import javax.validation.constraints.*;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/mpa")
public class RatingMpaController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<RatingMpa> getAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingMpa getMpaById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getMpaById(id);
    }
}