package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final List<Genre> genres = List.of(new Genre(1, "Комедия"), new Genre(2, "Драма"),
            new Genre(3, "Мультфильм"), new Genre(4, "Триллер"),
            new Genre(5, "Документальный"), new Genre(6, "Боевик"));
    private final List<RatingMpa> ratingMpa = List.of(new RatingMpa(1, "G"), new RatingMpa(2, "PG"),
            new RatingMpa(3, "PG-13"), new RatingMpa(4, "R"), new RatingMpa(5, "NC-17"));
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
                .sorted((film1, film2) -> film2.getLike().size() - film1.getLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return genres;
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genres.get(id - 1);
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        return ratingMpa;
    }

    @Override
    public RatingMpa getMpaById(Integer id) {
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