package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/mpa")
public class RatingMpaController {
    private final FilmService filmService;

    @GetMapping
    public Collection<RatingMpa> getAllMpa() {
        return filmService.getAllMpa();
    }

    @GetMapping("/{id}")
    public RatingMpa getMpaById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getMpaById(id);
    }
}