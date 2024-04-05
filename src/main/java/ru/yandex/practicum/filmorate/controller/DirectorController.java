package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/directors")
public class DirectorController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.getDirectorById(id);
    }

    @GetMapping
    public Collection<Director> getAllDirectors() {
        return filmService.getAllDirectors();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director createDirector(@Valid @RequestBody Director director) {
        return filmService.createDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return filmService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public String deleteDirector(@PathVariable @NotNull @Min(1) Integer id) {
        return filmService.deleteDirector(id);
    }
}