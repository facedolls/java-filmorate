package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }
    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public void deleteFilm(Integer id) {
        films.remove(id);
    }
}
