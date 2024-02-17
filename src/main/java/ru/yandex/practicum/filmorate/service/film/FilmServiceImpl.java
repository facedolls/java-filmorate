package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public Film getFilmById(Integer id) {
        checkFilmExistence(id);
        return filmStorage.getFilmsById(id);
    }

    private void checkFilmExistence(Integer id) {
        if (filmStorage.getFilmsById(id) == null) {
            log.warn("Film with id={} not found", id);
            throw new FilmNotFoundException(String.format("Film with id=%d not found", id));
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Received all films");
        return filmStorage.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        log.info("Received popular films");
        return filmStorage.getPopularFilm(count);
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
        checkFilmExistence(film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Update film {}", film);
        return updatedFilm;
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        Film film = getFilmById(id);
        film.getLike().add(userId);
        filmStorage.updateFilm(film);
        log.info("User userId={} liked the film id={}", userId, id);
        return film;
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        Film film = getFilmById(id);
        film.getLike().remove(userId);
        filmStorage.updateFilm(film);
        log.info("User id={} removed the like from the film id={}", userId, id);
        return film;
    }

    @Override
    public String deleteFilm(Integer id) {
        checkFilmExistence(id);
        filmStorage.deleteFilm(id);
        log.info("Film with id={} removed", id);
        return String.format("Film with id=%d removed", id);
    }
}