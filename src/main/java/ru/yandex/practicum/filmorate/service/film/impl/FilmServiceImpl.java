package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.model.feedEvent.*;
import ru.yandex.practicum.filmorate.service.feedEvent.FeedEventService;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final FeedEventService feedEventService;
    private final UserService userService;

    @Override
    public Film getFilmById(Long id) {
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
    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        log.info("Received populars films");
        return filmStorage.getPopularFilm(count, genreId, year);
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
        if (film.getId() != null) {
            log.warn("Incorrect id={} was passed when creating the film: ", film.getId());
            throw new ValidationException("id for the film must not be specified");
        }
        isExistsRatingMpa(film);
        isExistsGenres(film);
        film.getDirectors().forEach(director -> isExistsIdDirector(director.getId()));
        Film filmCreated = filmStorage.createFilm(film);
        log.info("Create film {}", filmCreated);
        return filmCreated;
    }

    private void isExistsRatingMpa(Film film) {
        RatingMpa mpa = filmStorage.getMpaById(film.getMpa().getId());
        if (mpa == null) {
            log.warn("Rating MPA with id={} not already exist", film.getMpa().getId());
            throw new ValidationException(String.format(
                    "Rating MPA with id=%d not already exist", film.getMpa().getId()));
        }
    }

    private void isExistsGenres(Film film) {
        film.getGenres().forEach(genreFilm -> {
            Genre genre = filmStorage.getGenreById(genreFilm.getId());
            if (genre == null) {
                log.warn("Genre with id={} not already exist", genreFilm.getId());
                throw new ValidationException(String.format(
                        "Genre with id=%d not already exist", genreFilm.getId()));
            }
        });
    }

    private void isExistsIdDirector(Integer directorId) {
        Director director = filmStorage.getDirectorById(directorId);
        if (director == null) {
            log.warn("Director with id={} not already exist", directorId);
            throw new ValidationException(String.format("Director with id=%d not already exist", directorId));
        }
    }

    @Override
    public Film updateFilm(Film film) {
        isExistsIdFilm(film.getId());
        isExistsRatingMpa(film);
        isExistsGenres(film);
        film.getDirectors().forEach(director -> isExistsIdDirector(director.getId()));
        Film filmUpdated = filmStorage.updateFilm(film);
        log.info("Update film {}", filmUpdated);
        return filmUpdated;
    }

    @Override
    public void isExistsIdFilm(Long filmId) {
        boolean isExists = filmStorage.isExistsIdFilm(filmId);
        if (!isExists) {
            log.warn("Film with id={} not found", filmId);
            throw new FilmNotFoundException(String.format("Film with id=%d not found", filmId));
        }
    }

    @Override
    public Film putLike(Long id, Long userId, Integer grade) {
        isExistsIdFilm(id);
        feedEventService.addFeedEvent(userId, EventType.LIKE, EventOperation.ADD, id);
        log.info("User userId={} liked the film id={}", userId, id);
        return filmStorage.putLike(id, userId, grade);
    }

    @Override
    public Film deleteLike(Long id, Long userId) {
        isExistsIdFilm(id);
        feedEventService.addFeedEvent(userId, EventType.LIKE, EventOperation.REMOVE, id);
        log.info("User id={} removed the like from the film id={}", userId, id);
        return filmStorage.deleteLike(id, userId);
    }

    @Override
    public String deleteFilm(Long id) {
        isExistsIdFilm(id);
        filmStorage.deleteFilm(id);
        log.info("Film with id={} deleted", id);
        return String.format("Film with id=%d deleted", id);
    }

    @Override
    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        userService.isExistsIdUser(userId);
        userService.isExistsIdUser(friendId);
        log.info("Received common films between user id={} and user id={}", userId, friendId);
        return filmStorage.getCommonFilms(userId, friendId);
    }

    @Override
    public Collection<Film> getFilmsByDirector(Integer directorId, String sortBy) {
        getDirectorById(directorId);
        if (sortBy.equals("likes") || sortBy.equals("year")) {
            log.info("Received films by director with id={}", directorId);
            return filmStorage.getFilmsByDirector(directorId, sortBy);
        }
        log.warn("Unknown argument value sortBy={}", sortBy);
        throw new IllegalArgumentException("Argument value \"sortBy\" should be likes or year");
    }

    @Override
    public Collection<Director> getAllDirectors() {
        log.info("Received all directors");
        return filmStorage.getAllDirectors();
    }

    @Override
    public Director getDirectorById(Integer directorId) {
        Director director = filmStorage.getDirectorById(directorId);
        if (director == null) {
            log.warn("Director with id={} not found", directorId);
            throw new NotFoundException(String.format("Director with id=%d not found", directorId));
        }
        return director;
    }

    @Override
    public Director createDirector(Director director) {
        log.info("Create director={}", director);
        return filmStorage.createDirector(director);
    }

    @Override
    public Director updateDirector(Director director) {
        getDirectorById(director.getId());
        log.info("Update director={}", director);
        return filmStorage.updateDirector(director);
    }

    @Override
    public String deleteDirector(Integer directorId) {
        getDirectorById(directorId);
        filmStorage.deleteDirector(directorId);
        log.info("Director with id={} deleted", directorId);
        return String.format("Director with id=%d deleted", directorId);
    }

    @Override
    public Collection<Film> searchFilms(String query, String by) {
        if (by.indexOf(',') > 0) {
            String[] findByArray = by.split(",");
            if (findByArray.length > 2) {
                log.warn("The number of parameters is incorrect {}", by);
                throw new ValidationException("The number of parameters is incorrect!");
            } else if (!(findByArray[0].equals("title") || findByArray[0].equals("director"))) {
                log.warn("The first parameter {} is incorrect!", findByArray[0]);
                throw new ValidationException("The first parameter " + findByArray[0] + " is incorrect!");
            } else if (!(findByArray[1].equals("title") || findByArray[1].equals("director"))) {
                log.warn("The second parameter {} is incorrect!", findByArray[1]);
                throw new ValidationException("The second parameter " + findByArray[1] + " is incorrect!");
            } else {
                return filmStorage.searchFilmsByTitleAndDirector(query);
            }
        } else {
            if (by.equals("title")) {
                return filmStorage.searchFilmsByTitle(query);
            } else if (by.equals("director")) {
                return filmStorage.searchFilmsByDirector(query);
            } else {
                log.warn("The parameter {} is incorrect", by);
                throw new ValidationException("The parameter " + by + " is incorrect");
            }
        }
    }
}