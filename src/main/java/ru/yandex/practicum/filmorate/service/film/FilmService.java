package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import java.util.Collection;

public interface FilmService {
    Film getFilmById(Integer id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count);

    Collection<Genre> getAllGenres();

    Genre getGenreById(Integer id);

    Collection<RatingMpa> getAllMpa();

    RatingMpa getMpaById(Integer id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film putLike(Integer id, Long userId);

    Film deleteLike(Integer id, Long userId);

    String deleteFilm(Integer id);
}