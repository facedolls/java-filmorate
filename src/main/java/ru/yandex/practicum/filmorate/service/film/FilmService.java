package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmService {
    Film checkFilmExistenceAndGetFilmById(Integer id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film putLike(Integer id, Long userId);

    Film deleteLike(Integer id, Long userId);

    String deleteFilm(Integer id);
}