package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getFilmById(id));
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getAllFilms());
    }

    @GetMapping("/popular")
    public ResponseEntity<Collection<Film>> getPopularFilm(@RequestParam(defaultValue = "10",
            required = false) Integer count) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getPopularFilm(count));
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.createFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.updateFilm(film));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> putLike(@PathVariable Integer id, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.putLike(id, userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable Integer id, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.deleteLike(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFilm(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.deleteFilm(id));
    }
}