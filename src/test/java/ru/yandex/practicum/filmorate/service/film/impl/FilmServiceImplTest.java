package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.feedEvent.FeedEventService;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceImplTest {
    private FilmService filmService;
    private FeedEventService feedEventService;
    private UserService userService;
    private final FilmStorage filmStorage;
    private Film film1;

    @BeforeEach
    public void setUp() {
        filmService = new FilmServiceImpl(filmStorage, feedEventService, userService);
        film1 = new Film("555", "555", LocalDate.of(2010, 11, 15), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")), new ArrayList<>());
    }

    @DisplayName("Должен выдать исключение FilmNotFoundException и не найти id фильма")
    @Test
    public void shouldNotFindTheFilmId() {
        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> filmService.getFilmById(25)
        );
        assertEquals("Film with id=25 not found", exception.getMessage());
    }

    @DisplayName("Должен выдать исключение NotFoundException и не найти id жанра")
    @Test
    public void shouldNotFindTheGenreId() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.getGenreById(25)
        );
        assertEquals("Genre with id=25 not found", exception.getMessage());
    }

    @DisplayName("Должен выдать исключение NotFoundException и не найти id рейтинга МПА")
    @Test
    public void shouldNotFindTheRatingMpaId() {
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.getMpaById(25)
        );
        assertEquals("Rating MPA with id=25 not found", exception.getMessage());
    }

    @DisplayName("Не должен обновить фильм и должен выдать исключение FilmNotFoundException")
    @Test
    public void shouldNotUpdateFilmDueToNonExistentId() {
        Film film = new Film(60,"60", "60",
                LocalDate.of(2000, 12, 12), 60,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")), new ArrayList<>());

        FilmNotFoundException exception = assertThrows(
                FilmNotFoundException.class,
                () -> filmService.updateFilm(film)
        );
        assertEquals("Film with id=60 not found", exception.getMessage());
    }

    @DisplayName("Не должен обновить фильм и должен выдать исключение ValidationException")
    @Test
    public void shouldNotUpdateFilmDueToNonExistentRatingMpa() {
        filmService.createFilm(film1);
        Film film = new Film(1,"60", "60",
                LocalDate.of(2000, 12, 12), 60,
                new RatingMpa(1000, "GKLJH"), List.of(new Genre(1, "Комедия")), new ArrayList<>());

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.updateFilm(film)
        );
        assertEquals("Rating MPA with id=1000 not already exist", exception.getMessage());
    }

    @DisplayName("Не должен найти фильм и должен выдать исключение ValidationException")
    @Test
    public void shouldNotFindFilmWithIncorrectNumberOfParameters() {
        filmService.createFilm(film1);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.searchFilms("555", "title,director,somethingElse")
        );
        assertEquals("The number of parameters is incorrect!", exception.getMessage());
    }

    @DisplayName("Не должен найти фильм и должен выдать исключение ValidationException")
    @Test
    public void shouldNotFindFilmWithIncorrectFirstParameter() {
        filmService.createFilm(film1);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.searchFilms("555", "wrongTitle,director")
        );
        assertEquals("The first parameter wrongTitle is incorrect!", exception.getMessage());
    }

    @DisplayName("Не должен найти фильм и должен выдать исключение ValidationException")
    @Test
    public void shouldNotFindFilmWithIncorrectSecondParameter() {
        filmService.createFilm(film1);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.searchFilms("555", "title,wrongDirector")
        );
        assertEquals("The second parameter wrongDirector is incorrect!", exception.getMessage());
    }

    @DisplayName("Не должен найти фильм и должен выдать исключение ValidationException")
    @Test
    public void shouldNotFindFilmWithIncorrectParameter() {
        filmService.createFilm(film1);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmService.searchFilms("555", "wrongTitle")
        );
        assertEquals("The parameter wrongTitle is incorrect", exception.getMessage());
    }
}