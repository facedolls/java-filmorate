package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorageDb;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceDbImpl implements FilmService {
    private final FilmStorageDb filmStorage;

    @Override
    public Film getFilmById(Integer id) {
        return filmStorage.getFilmsById(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        return filmStorage.getPopularFilm(count);
    }

    @Override
    public Collection<String> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    @Override
    public String getGenreById(Integer id) {
        return filmStorage.getGenreById(id);
    }

    @Override
    public Collection<String> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    @Override
    public String getMpaById(Integer id) {
        return filmStorage.getMpaById(id);
    }

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        return filmStorage.putLike(id, userId);
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        return filmStorage.deleteLike(id, userId);
    }

    @Override
    public String deleteFilm(Integer id) {
        filmStorage.deleteFilm(id);
        log.info("Film with id={} deleted", id);
        return String.format("Film with id=%d deleted", id);
    }
}