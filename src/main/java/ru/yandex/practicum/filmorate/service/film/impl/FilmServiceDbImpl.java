package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDb;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class FilmServiceDbImpl implements FilmService {
    private final FilmStorageDb filmStorage;

    @Override
    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmsById(id);
        if (film == null) {
            log.warn("Film with id={} not found", id);
            throw new FilmNotFoundException(String.format("Film with id=%d not found", id));
        }
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Received all films");
        return filmStorage.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        log.info("Received populars films");
        return filmStorage.getPopularFilm(count);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        log.info("Received all genres");
        return filmStorage.getAllGenres();
    }

    @Override
    public Genre getGenreById(Integer id) {
        Genre genre = filmStorage.getGenreById(id);
        if (genre == null) {
            log.warn("Genre with id={} not found", id);
            throw new NotFoundException(String.format("Genre with id=%d not found", id));
        }
        return genre;
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        log.info("Received all ratings mpa");
        return filmStorage.getAllMpa();
    }

    @Override
    public RatingMpa getMpaById(Integer id) {
        RatingMpa mpa = filmStorage.getMpaById(id);
        if (mpa == null) {
            log.warn("Rating MPA with id={} not found", id);
            throw new NotFoundException(String.format("Rating MPA with id=%d not found", id));
        }
        return mpa;
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getId() != 0) {
            log.warn("Film already exist {}", film);
            throw new FilmAlreadyExistException(String.format(
                    "Film id=%d \"%s\"already exist", film.getId(),film.getName()));
        }
        Film filmCreated = filmStorage.createFilm(film);
        log.info("Create film {}", filmCreated);
        return filmCreated;
    }

    @Override
    public Film updateFilm(Film film) {
        isExistsId(film.getId());
        Film filmUpdated = filmStorage.updateFilm(film);
        log.info("Update film {}", filmUpdated);
        return filmUpdated;
    }

    private void isExistsId(Integer filmId) {
        boolean isExists = filmStorage.isExistsId(filmId);
        if (!isExists) {
            log.warn("Film with id={} not found", filmId);
            throw new FilmNotFoundException(String.format("Film with id=%d not found", filmId));
        }
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        isExistsId(id);
        log.info("User userId={} liked the film id={}", userId, id);
        return filmStorage.putLike(id, userId);
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        isExistsId(id);
        log.info("User id={} removed the like from the film id={}", userId, id);
        return filmStorage.deleteLike(id, userId);
    }

    @Override
    public String deleteFilm(Integer id) {
        isExistsId(id);
        filmStorage.deleteFilm(id);
        log.info("Film with id={} deleted", id);
        return String.format("Film with id=%d deleted", id);
    }
}