package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

public interface FilmStorageDb extends FilmStorage {
    Film putLike(Integer id, Long userId);

    Film deleteLike(Integer id, Long userId);

    boolean isExistsIdFilm(Integer filmId);
}
