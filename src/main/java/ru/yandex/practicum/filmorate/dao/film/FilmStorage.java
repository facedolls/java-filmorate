package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film getFilmsById(Long id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year);

    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Collection<RatingMpa> getAllMpa();

    RatingMpa getMpaById(Integer id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Long id);

    Film putLike(Long id, Long userId);

    Film deleteLike(Long id, Long userId);

    boolean isExistsIdFilm(Long filmId);

    Collection<Film> getCommonFilms(Long userId, Long friendId);

    Collection<Film> getFilmsByDirector(Integer directorId, String sortBy);

    Collection<Director> getAllDirectors();

    Director getDirectorById(Integer directorId);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(Integer directorId);

    List<Film> searchFilmsByTitle(String query);

    List<Film> searchFilmsByDirector(String query);

    List<Film> searchFilmsByTitleAndDirector(String query);
}