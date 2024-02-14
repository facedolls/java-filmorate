package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {
    Film getFilmById(Integer id);
    Collection<Film> getAllFilms();
    Film createFilm(Film film);
    Film updateFilm(Film film);
    void deleteFilm(Integer id);
}