package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Genre> genres = Map.of(
            1, new Genre(1, "Комедия"), 2, new Genre(2, "Драма"),
            3, new Genre(3, "Мультфильм"), 4, new Genre(4, "Триллер"),
            5, new Genre(5, "Документальный"), 6, new Genre(6, "Боевик"));
    private final Map<Integer, RatingMpa> ratingMpa = Map.of(1, new RatingMpa(1, "G"),
            2, new RatingMpa(2, "PG"), 3, new RatingMpa(3, "PG-13"),
            4, new RatingMpa(4, "R"), 5, new RatingMpa(5, "NC-17"));
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
        return genres.values().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenreById(Integer id) {
        if (genres.containsKey(id)) {
            return genres.get(id);
        }
        return null;
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        return ratingMpa.values().stream()
                .sorted(Comparator.comparing(RatingMpa::getId))
                .collect(Collectors.toList());
    }

    @Override
    public RatingMpa getMpaById(Integer id) {
        if (ratingMpa.containsKey(id)) {
            return ratingMpa.get(id);
        }
        return null;
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

    @Override
    public Film putLike(Integer id, Long userId) {
        if (films.containsKey(id)) {
            Film film = films.get(id);
            Set<Long> like = film.getLike();
            like.add(userId);
            films.put(id, film);
            return film;
        }
        return null;
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        if (films.containsKey(id)) {
            Film film = films.get(id);
            Set<Long> like = film.getLike();
            like.remove(userId);
            films.put(id, film);
            return film;
        }
        return null;
    }

    @Override
    public boolean isExistsIdFilm(Integer filmId) {
        return films.containsKey(filmId);
    }
}