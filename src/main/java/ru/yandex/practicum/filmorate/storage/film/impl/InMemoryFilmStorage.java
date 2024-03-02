package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final List<String> genres = Arrays.asList("Комедия", "Драма", "Мультфильм", "Триллер",
            "Документальный", "Боевик");
    private final List<String> ratingMpa = Arrays.asList("G", "PG", "PG-13", "R", "NC-17");
    private static int id = 1;

    @Override
    public Film getFilmsById(Integer id) {
        return films.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        return films.values().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> getAllGenres() {
        return genres;
    }

    @Override
    public String getGenreById(Integer id) {
        return genres.get(id - 1);
    }

    public Collection<String> getAllMpa() {
        return ratingMpa;
    }

    public String getMpaById(Integer id) {
        return ratingMpa.get(id - 1);
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public void deleteFilm(Integer id) {
        films.remove(id);
    }
}