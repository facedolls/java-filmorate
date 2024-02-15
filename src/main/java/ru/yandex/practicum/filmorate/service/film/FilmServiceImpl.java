package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public Film checkFilmExistenceAndGetFilmById(Integer id) {
        if (id == null || id < 1) {
            log.warn("Incorrect id={} passed for film", id);
            throw new IncorrectParameterException(String.format("Error with field id=%d for film", id));
        }
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Film with id={} not found", id);
                    throw new FilmNotFoundException(String.format("Film with id=%d not found", id));
                });
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Received all films");
        return filmStorage.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        if (count < 1) {
            log.warn("Incorrect count={} passed for films", count);
            throw new IncorrectParameterException(String.format("Error with field count=%d", count));
        }
        log.info("Received popular films");
        return filmStorage.getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLike().size() - film1.getLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film createFilm(Film film) {
        if (film.getId() != 0) {
            log.warn("Film already exist {}", film);
            throw new FilmAlreadyExistException(String.format(
                    "Film id=%d \"%s\"already exist", film.getId(),film.getName()));
        }
        Film createdFilm = filmStorage.createFilm(film);
        log.info("Create film {}", film);
        return createdFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        checkFilmExistenceAndGetFilmById(film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Update film {}", film);
        return updatedFilm;
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        if (userId == null || userId < 1) {
            log.warn("Incorrect userId={} passed for films", userId);
            throw new IncorrectParameterException(String.format("Error with field userId=%d", userId));
        }
        Film film = checkFilmExistenceAndGetFilmById(id);
        film.getLike().add(userId);
        filmStorage.updateFilm(film);
        log.info("User userId={} liked the film id={}", userId, id);
        return film;
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        if (userId == null || userId < 1) {
            log.warn("Incorrect userId={} passed for films id={}", userId, id);
            throw new IncorrectParameterException(String.format("Error with field userId=%d", userId));
        }
        Film film = checkFilmExistenceAndGetFilmById(id);
        film.getLike().remove(userId);
        filmStorage.updateFilm(film);
        log.info("User id={} removed the like from the film id={}", userId, id);
        return film;
    }

    @Override
    public String deleteFilm(Integer id) {
        checkFilmExistenceAndGetFilmById(id);
        filmStorage.deleteFilm(id);
        log.info("Film with id={} removed", id);
        return String.format("Film with id=%d removed", id);
    }
}