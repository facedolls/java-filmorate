package ru.yandex.practicum.filmorate.service.film;

import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.*;
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

    Collection<Film> getFilmsByDirector(Integer directorId, String sortBy);

    Collection<Director> getAllDirectors();

    Director getDirectorById(Integer id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    String deleteDirector(Integer id);

    Collection<Film> searchFilms(String query, String by);
}