package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.user.impl.UserStorageDbImpl;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmStorageDbImlTest {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private Film film1;
    private Film film2;
    private Film film3;
    private Film film4;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        filmStorage = new FilmStorageDbImpl(jdbcTemplate, parameter);
        userStorage = new UserStorageDbImpl(jdbcTemplate, parameter);
        film1 = new Film("8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")), new ArrayList<>());
        film2 = new Film("Собака киборг", "Собака спасает мир от инопланетян",
                LocalDate.of(2007, 9, 1), 60,
                new RatingMpa(2, "PG"), List.of(new Genre(6, "Боевик")), new ArrayList<>());
        film3 = new Film("Веселые ребята", "Трое друзей отправляются в путешествие",
                LocalDate.of(2013, 4, 26), 80,
                new RatingMpa(3, "PG-13"), List.of(new Genre(1, "Комедия")), new ArrayList<>());
        film4 = new Film("Хитрый лис", "Сказка о лисенке",
                LocalDate.of(2010, 7, 3), 70, new RatingMpa(1, "G"),
                List.of(new Genre(3, "Мультфильм"), new Genre(1, "Комедия")), new ArrayList<>());
        user1 = new User("petrov@email.ru", "vanya123", "Иван Петров",
                LocalDate.of(1990, 1, 1));
        user2 = new User("livanova@email.ru", "liv4mar123", "Мария Ливанова",
                LocalDate.of(1994, 9, 17));
        user3 = new User("nikitin@email.ru", "sr4nik123", "Сергей Никитин",
                LocalDate.of(2000, 12, 24));
    }

    @DisplayName("Должен создать фильм")
    @Test
    public void shouldCreateFilm() {
        Film film = new Film(1, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"),
                List.of(new Genre(1, "Комедия")), new ArrayList<>());
        filmStorage.createFilm(film1);

        Film result = filmStorage.getFilmsById(1);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @DisplayName("Должен обновить фильм")
    @Test
    public void shouldUpdateFilm() {
        Film filmForUpdate = new Film(1,"миля", "Смит",
                LocalDate.of(2003, 12, 7), 100,
                new RatingMpa(2, "PG"), List.of(new Genre(2, "Драма")), new ArrayList<>());
        filmStorage.createFilm(film1);
        filmStorage.updateFilm(filmForUpdate);

        Film result = filmStorage.getFilmsById(1);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmForUpdate);
    }

    @DisplayName("Должен вернуть фильм по id")
    @Test
    public void shouldReturnFilmById() {
        Film film = new Film(1, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110, new RatingMpa(5, "NC-17"),
                List.of(new Genre(1, "Комедия")), new ArrayList<>());
        filmStorage.createFilm(film1);

        Film result1 = filmStorage.getFilmsById(1);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);

        Film result2 = filmStorage.getFilmsById(84);
        assertThat(result2)
                .isNull();
    }

    @DisplayName("Должен вернуть жанр по id")
    @Test
    public void shouldReturnGenreById() {
        Genre genre = new Genre(3, "Мультфильм");

        Genre result = filmStorage.getGenreById(3);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }

    @DisplayName("Должен вернуть рейтинг MPA по id")
    @Test
    public void shouldReturnRatingMpaById() {
        RatingMpa rating = new RatingMpa(2, "PG");

        RatingMpa result = filmStorage.getMpaById(2);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(rating);
    }

    @DisplayName("Должен вернуть все жанры")
    @Test
    public void shouldReturnAllGenres() {
        List<Genre> genres = List.of(
                new Genre(1, "Комедия"), new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"), new Genre(4, "Триллер"),
                new Genre(5, "Документальный"), new Genre(6, "Боевик"));

        Collection<Genre> result = filmStorage.getAllGenres();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genres);
    }

    @DisplayName("Должен вернуть все рейтинги MPA")
    @Test
    public void shouldReturnAllRatingsMpa() {
        List<RatingMpa> ratings = List.of(
                new RatingMpa(1, "G"), new RatingMpa(2, "PG"),
                new RatingMpa(3, "PG-13"), new RatingMpa(4, "R"),
                new RatingMpa(5, "NC-17"));

        Collection<RatingMpa> result = filmStorage.getAllMpa();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(ratings);
    }

    @DisplayName("Должен вернуть все фильмы")
    @Test
    public void shouldReturnAllFilms() {
        Film filmNew1 = filmStorage.createFilm(film1);
        Film filmNew2 = filmStorage.createFilm(film2);
        Film filmNew3 = filmStorage.createFilm(film3);
        Film filmNew4 = filmStorage.createFilm(film4);
        List<Film> films = List.of(filmNew1, filmNew2, filmNew3, filmNew4);

        Collection<Film> result = filmStorage.getAllFilms();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @DisplayName("Должен вернуть 2 популярных фильма")
    @Test
    void shouldReturnPopularFilms() {
        Film filmResult1 = new Film(1, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")), new ArrayList<>());
        Film filmResult3 = new Film(3, "Веселые ребята", "Трое друзей отправляются в путешествие",
                LocalDate.of(2013, 4, 26), 80,
                new RatingMpa(3, "PG-13"), List.of(new Genre(1, "Комедия")), new ArrayList<>());
        List<Film> films = List.of(filmResult3, filmResult1);

        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        filmStorage.createFilm(film3);
        filmStorage.createFilm(film4);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        filmStorage.putLike(3, 1L);
        filmStorage.putLike(3, 2L);
        filmStorage.putLike(3, 3L);
        filmStorage.putLike(1, 1L);
        filmStorage.putLike(1, 3L);
        filmStorage.putLike(4, 2L);

        Collection<Film> result = filmStorage.getPopularFilm(2, 0, 0);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @DisplayName("Должен добавить лайк фильму с id = 1 от пользователя с id = 1")
    @Test
    void shouldPutLikeFilm() {
        Film film = new Film(1, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")), new ArrayList<>());

        filmStorage.createFilm(film1);
        userStorage.createUser(user1);

        Film result = filmStorage.putLike(1, 1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @DisplayName("Должен удалить лайк фильму с id = 1 от пользователя с id = 1")
    @Test
    void shouldDeleteLikeFilm() {
        Film film = new Film(1, "8 миля", "Джимми Смит",
                LocalDate.of(2002, 11, 6), 110,
                new RatingMpa(5, "NC-17"), List.of(new Genre(1, "Комедия")), new ArrayList<>());

        filmStorage.createFilm(film1);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        filmStorage.putLike(1, 1L);
        filmStorage.putLike(1, 2L);

        Film result = filmStorage.deleteLike(1, 1L);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @DisplayName("Должен удалить фильм с id = 1")
    @Test
    void shouldDeleteFilm() {
        List<Film> films = List.of(film2);
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        filmStorage.deleteFilm(1);

        Collection<Film> result = filmStorage.getAllFilms();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @DisplayName("Должен проверить существует ли id фильма")
    @Test
    void shouldCheckForExistenceOfFilmId() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);

        boolean resultFirst = filmStorage.isExistsIdFilm(1);
        assertThat(resultFirst)
                .isNotNull()
                .isEqualTo(true);

        boolean resultSecond = filmStorage.isExistsIdFilm(358);
        assertThat(resultSecond)
                .isNotNull()
                .isEqualTo(false);

    }

    @DisplayName("Должен вернуть всех режиссеров фильмов")
    @Test
    public void shouldReturnAllDirectors() {
        List<Director> director = List.of(new Director(1, "Director1"), new Director(2, "Director2"));
        filmStorage.createDirector(new Director(1, "Director1"));
        filmStorage.createDirector(new Director(2, "Director2"));

        Collection<Director> result = filmStorage.getAllDirectors();
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director);
    }

    @DisplayName("Должен вернуть режиссера фильмов по id")
    @Test
    public void shouldReturnDirectorById() {
        Director director = new Director(2, "Director2");
        filmStorage.createDirector(new Director(1, "Director1"));
        filmStorage.createDirector(director);

        Director result = filmStorage.getDirectorById(2);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director);
    }

    @DisplayName("Должен создать режиссера фильмов, игнорируя старый id и присвоив новый правильный id")
    @Test
    public void shouldCreateDirector() {
        Director director = new Director(2, "Director2");
        filmStorage.createDirector(new Director(365, "Director1"));
        filmStorage.createDirector(new Director(785, "Director2"));

        Director result = filmStorage.getDirectorById(2);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director);
    }

    @DisplayName("Должен обновить режиссера фильмов")
    @Test
    public void shouldUpdateDirector() {
        Director director1 = new Director(2, "Director2");
        filmStorage.createDirector(new Director(1, "Director1"));
        filmStorage.createDirector(director1);

        Director result1 = filmStorage.getDirectorById(2);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director1);

        Director director2 = new Director(2, "Director2 super");
        filmStorage.updateDirector(director2);

        Director result2 = filmStorage.getDirectorById(2);
        assertThat(result2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director2);
    }

    @DisplayName("Должен удалить режиссера фильмов")
    @Test
    public void shouldDeleteDirector() {
        List<Director> director1 = List.of(new Director(1, "Director1"), new Director(2, "Director2"));
        filmStorage.createDirector(new Director(1, "Director1"));
        filmStorage.createDirector(new Director(2, "Director2"));

        Collection<Director> result1 = filmStorage.getAllDirectors();
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director1);

        List<Director> director2 = List.of(new Director(2, "Director2"));
        filmStorage.deleteDirector(1);

        Collection<Director> result2 = filmStorage.getAllDirectors();
        assertThat(result2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director2);
    }

    @DisplayName("Должен создать фильм с режиссером")
    @Test
    public void shouldCreateFilmWithDirector() {
        filmStorage.createDirector(new Director(1, "Director"));
        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));

        Film result1 = filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, null))));

        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
    }

    @DisplayName("Должен обновить фильм с режиссером")
    @Test
    public void shouldUpdateFilmWithDirector() {
        filmStorage.createDirector(new Director(1, "Director"));
        filmStorage.createDirector(new Director(2, "Director2 super"));
        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));

        Film result1 = filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, null))));

        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);

        Film film2 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(2, "Director2 super")));

        Film result2 = filmStorage.updateFilm(new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(2, null))));

        assertThat(result2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
    }

    @DisplayName("Должен удалить режиссера у фильмов после удаления режиссера из базы данных")
    @Test
    public void shouldRemoveDirectorFromFilms() {
        filmStorage.createDirector(new Director(1, "Director"));
        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));

        Film result1 = filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, null))));

        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);

        Film result2 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                new ArrayList<>());

        filmStorage.deleteDirector(1);
        Film film2 = filmStorage.getFilmsById(1);

        assertThat(result2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
    }

    @DisplayName("Должен вернуть список фильмов по id режиссера и годам")
    @Test
    public void shouldReturnFilmsByDirectorAndYear() {
        filmStorage.createDirector(new Director(1, "Director"));
        filmStorage.createDirector(new Director(2, "Director2"));
        filmStorage.createDirector(new Director(3, "Director3"));

        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));
        Film film2 = new Film(2,"Ворона", "Фильм о белой вороне",
                LocalDate.of(2001, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2")));
        Film film3 = new Film(3,"Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")),
                List.of(new Director(1, "Director")));

        List<Film> listFilmsByDirectorAndYear = List.of(film1, film2, film3);

        filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"))));
        filmStorage.createFilm(new Film("Ворона", "Фильм о белой вороне",
                LocalDate.of(2001, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2"))));
        filmStorage.createFilm(new Film("Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")),
                List.of(new Director(1, "Director"))));

        Collection<Film> films = filmStorage.getFilmsByDirector(1, "year");
        assertThat(films)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByDirectorAndYear);
    }

    @DisplayName("Должен вернуть список фильмов по id режиссера и лайкам")
    @Test
    public void shouldReturnFilmsByDirectorAndLikes() {
        filmStorage.createDirector(new Director(1, "Director"));
        filmStorage.createDirector(new Director(2, "Director2"));
        filmStorage.createDirector(new Director(3, "Director3"));

        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));
        Film film2 = new Film(2,"Ворона", "Фильм о белой вороне",
                LocalDate.of(2001, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2")));
        Film film3 = new Film(3,"Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")),
                List.of(new Director(1, "Director")));

        List<Film> listFilmsByDirectorAndLikes = List.of(film2, film3, film1);

        filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"))));
        filmStorage.createFilm(new Film("Ворона", "Фильм о белой вороне",
                LocalDate.of(2001, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2"))));
        filmStorage.createFilm(new Film("Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(1, "Комедия"), new Genre(2, "Драма")),
                List.of(new Director(1, "Director"))));

        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        filmStorage.putLike(2, 1L);
        filmStorage.putLike(2, 2L);
        filmStorage.putLike(2, 3L);
        filmStorage.putLike(3, 1L);
        filmStorage.putLike(3, 2L);

        Collection<Film> films = filmStorage.getFilmsByDirector(1, "likes");
        assertThat(films)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByDirectorAndLikes);
    }

    @DisplayName("Должен вернуть популярные фильмы по id жанра")
    @Test
    void shouldReturnPopularFilmsByGenre() {
        filmStorage.createDirector(new Director(1, "Director"));
        filmStorage.createDirector(new Director(2, "Director2"));
        filmStorage.createDirector(new Director(3, "Director3"));

        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));
        Film film2 = new Film(2,"Ворона", "Фильм о белой вороне",
                LocalDate.of(2001, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2")));
        Film film3 = new Film(3,"Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director")));
        List<Film> listFilmsByGenre1 = List.of(film2, film1);
        List<Film> listFilmsByGenre2 = List.of(film3);

        filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"))));
        filmStorage.createFilm(new Film("Ворона", "Фильм о белой вороне",
                LocalDate.of(2001, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2"))));
        filmStorage.createFilm(new Film("Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director"))));

        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        filmStorage.putLike(3, 1L);
        filmStorage.putLike(3, 2L);
        filmStorage.putLike(3, 3L);
        filmStorage.putLike(2, 1L);
        filmStorage.putLike(2, 3L);
        filmStorage.putLike(1, 2L);

        Collection<Film> result = filmStorage.getPopularFilm(10,1, 0);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByGenre1);

        Collection<Film> result1 = filmStorage.getPopularFilm(10, 2, 0);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByGenre2);
    }

    @DisplayName("Должен вернуть популярные фильмы по году")
    @Test
    void shouldReturnPopularFilmsByYear() {
        filmStorage.createDirector(new Director(1, "Director"));
        filmStorage.createDirector(new Director(2, "Director2"));
        filmStorage.createDirector(new Director(3, "Director3"));

        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));
        Film film2 = new Film(2,"Ворона", "Фильм о белой вороне",
                LocalDate.of(2013, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2")));
        Film film3 = new Film(3,"Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director")));
        List<Film> listFilmsByYear1 = List.of(film1);
        List<Film> listFilmsByYear2 = List.of(film3, film2);

        filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"))));
        filmStorage.createFilm(new Film("Ворона", "Фильм о белой вороне",
                LocalDate.of(2013, 12, 30), 70,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"), new Director(2, "Director2"))));
        filmStorage.createFilm(new Film("Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director"))));

        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        filmStorage.putLike(3, 1L);
        filmStorage.putLike(3, 2L);
        filmStorage.putLike(3, 3L);
        filmStorage.putLike(2, 1L);
        filmStorage.putLike(2, 3L);
        filmStorage.putLike(1, 2L);

        Collection<Film> result = filmStorage.getPopularFilm(10,0, 2001);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByYear1);

        Collection<Film> result1 = filmStorage.getPopularFilm(10, 0, 2013);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByYear2);
    }

    @DisplayName("Должен вернуть популярные фильмы по году и id жанра")
    @Test
    void shouldReturnPopularFilmsByYearAndGenres() {
        filmStorage.createDirector(new Director(1, "Director"));
        filmStorage.createDirector(new Director(2, "Director2"));
        filmStorage.createDirector(new Director(3, "Director3"));

        Film film1 = new Film(1,"Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director")));
        Film film2 = new Film(2,"Ворона", "Фильм о белой вороне",
                LocalDate.of(2013, 12, 30), 70,
                new RatingMpa(1, "G"),
                List.of(new Genre(1, "Комедия"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director"), new Director(2, "Director2")));
        Film film3 = new Film(3,"Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director")));
        List<Film> listFilmsByGenreAndYear1 = List.of(film1);
        List<Film> listFilmsByGenreAndYear2 = List.of(film3, film2);
        List<Film> listFilmsByGenreAndYear3 = List.of(film2);

        filmStorage.createFilm(new Film("Кот и пес", "Фильм о дружбе",
                LocalDate.of(2001, 9, 3), 50,
                new RatingMpa(1, "G"), List.of(new Genre(1, "Комедия")),
                List.of(new Director(1, "Director"))));
        filmStorage.createFilm(new Film("Ворона", "Фильм о белой вороне",
                LocalDate.of(2013, 12, 30), 70,
                new RatingMpa(1, "G"),
                List.of(new Genre(1, "Комедия"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director"), new Director(2, "Director2"))));
        filmStorage.createFilm(new Film("Пряник", "Фильм о прянике, который съели",
                LocalDate.of(2013, 2, 17), 60,
                new RatingMpa(2, "PG"),
                List.of(new Genre(2, "Драма"), new Genre(3, "Мультфильм")),
                List.of(new Director(1, "Director"))));

        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        filmStorage.putLike(3, 1L);
        filmStorage.putLike(3, 2L);
        filmStorage.putLike(3, 3L);
        filmStorage.putLike(2, 1L);
        filmStorage.putLike(2, 3L);
        filmStorage.putLike(1, 2L);

        Collection<Film> result = filmStorage.getPopularFilm(10,1, 2001);
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByGenreAndYear1);

        Collection<Film> result1 = filmStorage.getPopularFilm(10, 3, 2013);
        assertThat(result1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByGenreAndYear2);

        Collection<Film> result2 = filmStorage.getPopularFilm(10, 1, 2013);
        assertThat(result2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(listFilmsByGenreAndYear3);
    }
}