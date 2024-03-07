package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDb;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Primary
public class FilmStorageDbImpl implements FilmStorageDb {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilmsById(Integer id) {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "f.rating_id, r.name AS rating_name, l.user_id, fg.genre_id, g.name AS genre_name " +
                "FROM film AS f " +
                "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON g.genre_id = fg.genre_id " +
                "WHERE f.film_id = ?";
        return jdbcTemplate.query(sqlQuery, new FilmMapper(), id).stream().findAny().orElse(null);
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "f.rating_id, r.name AS rating_name, l.user_id, fg.genre_id, g.name AS genre_name " +
                "FROM film AS f " +
                "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON g.genre_id = fg.genre_id " +
                "ORDER BY f.film_id";
        return jdbcTemplate.query(sqlQuery, new FilmMapper());
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
       String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
               "f.rating_id, r.name AS rating_name, l.user_id, fg.genre_id, g.name AS genre_name " +
               "FROM film AS f " +
               "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
               "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
               "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
               "LEFT JOIN genre AS g ON g.genre_id = fg.genre_id " +
               "WHERE f.film_id IN (" +
               "SELECT fm.film_id FROM film AS fm " +
               "left JOIN favorite_film AS ff ON fm.film_id=ff.film_id " +
               "GROUP BY fm.film_id " +
               "ORDER BY COUNT(fm.film_id) DESC " +
               "LIMIT ?)";
        return jdbcTemplate.query(sqlQuery, new FilmMapper(), count);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")));
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("name")), id).stream()
                .findFirst().orElse(null);
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM rating";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new RatingMpa(rs.getInt("rating_id"),
                rs.getString("name")));
    }

    @Override
    public RatingMpa getMpaById(Integer id) {
        String sqlQuery = "SELECT * FROM rating WHERE rating_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new RatingMpa(rs.getInt("rating_id"),
                rs.getString("name")), id).stream().findFirst().orElse(null);
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert insertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> params = Map.of("name", film.getName(),
                "description", film.getDescription(), "release_date", film.getReleaseDate().toString(),
                "duration", film.getDuration(), "rating_id", film.getMpa().getId());
        film.setId(insertFilm.executeAndReturnKey(params).intValue());

        if (!film.getGenres().isEmpty()) {
            film.getGenres().forEach(genre ->
                    jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                            film.getId(), genre.getId()));
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        if (film.getGenres().isEmpty()) {
            jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
        } else {
            jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", film.getId());
            film.getGenres().forEach(genre ->
                    jdbcTemplate.update("INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                            film.getId(), genre.getId()));
        }
        return getFilmsById(film.getId());
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        String sqlQuery = "INSERT INTO favorite_film (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
        return getFilmsById(id);
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        String sqlQuery = "DELETE FROM favorite_film WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
        return getFilmsById(id);
    }

    @Override
    public void deleteFilm(Integer id) {
        String sqlQuery = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public boolean isExistsId(Integer filmId) {
        String sqlQuery = "SELECT film_id FROM film WHERE film_id = ?";
        List<Object> id = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getInt("film_id"), filmId);
        return id.size() == 1;
    }
}