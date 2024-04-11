package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.*;
import java.util.Collection;

public interface FilmService {
    Film getFilmById(Long id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year);

    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Collection<RatingMpa> getAllMpa();

    RatingMpa getMpaById(Integer id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film putLike(Long id, Long userId, Integer grade);

    Film deleteLike(Long id, Long userId);

    String deleteFilm(Long id);

    Collection<Film> getCommonFilms(Long userId, Long friendId);

    Collection<Film> getFilmsByDirector(Integer directorId, String sortBy);

    Collection<Director> getAllDirectors();

    Director getDirectorById(Integer id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    String deleteDirector(Integer id);

    void isExistsIdFilm(Long filmId);

    Collection<Film> searchFilms(String query, String by);
}