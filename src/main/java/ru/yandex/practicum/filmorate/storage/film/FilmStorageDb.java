package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorageDb extends FilmStorage {
    Film putLike(Integer id, Long userId);

    Film deleteLike(Integer id, Long userId);
}
