package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmService {
    Film getFilmById(Integer id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count);

    Collection<String> getAllGenres();

    String getGenreById(Integer id);

    Collection<String> getAllMpa();

    String getMpaById(Integer id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film putLike(Integer id, Long userId);

    Film deleteLike(Integer id, Long userId);

    String deleteFilm(Integer id);
}