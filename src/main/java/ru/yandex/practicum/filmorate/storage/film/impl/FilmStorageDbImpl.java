package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorageDb;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class FilmStorageDbImpl implements FilmStorageDb {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilmsById(Integer id) {
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        return null;
    }

    @Override
    public Collection<String> getAllGenres() {
        return null;
    }

    @Override
    public String getGenreById(Integer id) {
        return null;
    }

    @Override
    public Collection<String> getAllMpa() {
        return null;
    }

    @Override
    public String getMpaById(Integer id) {
        return null;
    }

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        return null;
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        return null;
    }

    @Override
    public void deleteFilm(Integer id) {

    }
}