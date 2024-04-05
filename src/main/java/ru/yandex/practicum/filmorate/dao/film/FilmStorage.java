package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.*;
import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film getFilmsById(Integer id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year);

    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Collection<RatingMpa> getAllMpa();

    RatingMpa getMpaById(Integer id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer id);

    Film putLike(Integer id, Long userId);

    Film deleteLike(Integer id, Long userId);

    boolean isExistsIdFilm(Integer filmId);

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