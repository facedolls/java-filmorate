package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.film.FilmStorageDb;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
public class FilmStorageDbImpl implements FilmStorageDb {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;

    @Override
    public Film getFilmsById(Integer id) {
        String sqlQuery = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
                "f.rating_id, r.name AS rating_name, l.user_id, fg.genre_id, g.name AS genre_name " +
                "FROM film AS f " +
                "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON g.genre_id = fg.genre_id " +
                "WHERE f.film_id = :filmId";
        return parameter.query(sqlQuery, Map.of("filmId", id), new FilmMapper()).stream()
                .findAny().orElse(null);
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
        return parameter.query(sqlQuery, new FilmMapper());
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
               "left JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
               "GROUP BY fm.film_id " +
               "ORDER BY COUNT(fm.film_id) DESC " +
               "LIMIT :count)";
        return parameter.query(sqlQuery, Map.of("count", count), new FilmMapper()).stream()
                .sorted((film1, film2) -> film2.getLike().size() - film1.getLike().size())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genre";
        return parameter.query(sqlQuery,
                (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")))
                .stream().sorted(Comparator.comparingInt(Genre::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sqlQuery = "SELECT * FROM genre WHERE genre_id = :genreId";
        return parameter.query(sqlQuery,
                        Map.of("genreId", id), (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                                rs.getString("name"))).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM rating";
        return parameter.query(sqlQuery,
                (rs, rowNum) -> new RatingMpa(rs.getInt("rating_id"), rs.getString("name")))
                .stream().sorted(Comparator.comparingInt(RatingMpa::getId))
                .collect(Collectors.toList());
    }

    @Override
    public RatingMpa getMpaById(Integer id) {
        String sqlQuery = "SELECT * FROM rating WHERE rating_id = :ratingId";
        return parameter.query(sqlQuery,
                Map.of("ratingId", id), (rs, rowNum) -> new RatingMpa(rs.getInt("rating_id"),
                        rs.getString("name"))).stream().findFirst().orElse(null);
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
        updateGenre(film);
        return getFilmsById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE film SET name = :name, description = :description, " +
                "release_date = :release_date, duration = :duration, rating_id = :ratingId " +
                "WHERE film_id = :filmId";
        Map<String, Object> params = Map.of("name", film.getName(), "description", film.getDescription(),
                "release_date", film.getReleaseDate(), "duration", film.getDuration(),
                "ratingId", film.getMpa().getId(), "filmId", film.getId());
        parameter.update(sqlQuery, params);
        parameter.update("DELETE FROM film_genre WHERE film_id = :filmId", Map.of("filmId", film.getId()));
        updateGenre(film);
        return getFilmsById(film.getId());
    }

    private void updateGenre(Film film) {
        if (!film.getGenres().isEmpty()) {
            film.getGenres().forEach(genre ->
                    parameter.update("INSERT INTO film_genre VALUES (:filmId, :genreId)",
                            Map.of("filmId", film.getId(), "genreId", genre.getId())));
        }
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        String sqlQuery = "INSERT INTO favorite_film VALUES (:filmId, :userId)";
        parameter.update(sqlQuery, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        String sqlQuery = "DELETE FROM favorite_film WHERE film_id = :filmId AND user_id = :userId";
        parameter.update(sqlQuery, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public void deleteFilm(Integer id) {
        String sqlQuery = "DELETE FROM film WHERE film_id = :filmId";
        parameter.update(sqlQuery, Map.of("filmId", id));
    }

    @Override
    public boolean isExistsIdFilm(Integer filmId) {
        String sqlQuery = "SELECT film_id FROM film WHERE film_id = :filmId";
        List<Object> id = parameter.query(sqlQuery,
                Map.of("filmId", filmId),
                (rs, rowNum) -> rs.getInt("film_id"));
        return id.size() == 1;
    }
}