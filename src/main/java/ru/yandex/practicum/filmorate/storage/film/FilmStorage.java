package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {
    Film getFilmsById(Integer id);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilm(Integer count);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Integer id);
}