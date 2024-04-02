package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.dao.film.FilmStorage;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
public class FilmStorageDbImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    protected final String sqlSelectOneFilm = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "WHERE film_id = :filmId";
    protected final String sqlSelectGenresToOneFilm = "SELECT f.*, g.name " +
            "FROM film_genre AS f " +
            "JOIN genre AS g ON f.genre_id = g.genre_id " +
            "WHERE film_id = :filmId";

    protected final String sqlSelectAllFilms = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "GROUP BY film_id, rating_name " +
            "ORDER BY film_id";

    protected final String sqlSelectGenresAllFilms = "SELECT f.*, g.name " +
            "FROM film_genre AS f " +
            "JOIN genre AS g ON f.genre_id = g.genre_id";
    protected final String sqlSelectPopularsFilms = "SELECT f.*, r.name AS rating_name, " +
            "COUNT(l.user_id) AS count_likes " +
            "FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(fm.film_id) DESC " +
            "LIMIT :count) " +
            "GROUP by f.film_id, rating_name " +
            "ORDER BY count_likes DESC";
    protected final String sqlSelectAllGenes = "SELECT * FROM genre ORDER BY genre_id";
    protected final String sqlSelectAllRatingMpa = "SELECT * FROM rating ORDER BY rating_id";
    protected final String sqlSelectToOneGenre = "SELECT * FROM genre WHERE genre_id = :genreId";
    protected final String sqlSelectToOneRatingMpa = "SELECT * FROM rating WHERE rating_id = :ratingId";
    protected final String sqlUpdateFilm = "UPDATE film SET name = :name, description = :description, " +
            "release_date = :release_date, duration = :duration, rating_id = :rating_id " +
            "WHERE film_id = :filmId";
    protected final String sqlDeleteGenresFilm = "DELETE FROM film_genre WHERE film_id = :filmId";
    protected final String sqlInsertGenresFilm = "INSERT INTO film_genre VALUES (:filmId, :genreId)";
    protected final String sqlInsertLikeFilm = "INSERT INTO favorite_film VALUES (:filmId, :userId)";
    protected final String sqlDeleteLikeFilm = "DELETE FROM favorite_film " +
            "WHERE film_id = :filmId AND user_id = :userId";
    protected final String sqlDeleteFilm = "DELETE FROM film WHERE film_id = :filmId";
    protected final String sqlSelectIdFilm = "SELECT film_id FROM film WHERE film_id = :filmId";
    protected final String sqlSelectPopularFilmsGenres = "SELECT f.*, g.name, COUNT(user_id) as count_likes " +
            "FROM film_genre AS f " +
            "LEFT JOIN genre AS g ON f.genre_id = g.genre_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(fm.film_id) DESC " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.genre_id, g.name";

    @Override
    public Film getFilmsById(Integer id) {
        Map<String, Object> params = Map.of("filmId", id);
        List<Film> film = parameter.query(sqlSelectOneFilm, params, new FilmMapper());

        if (film.isEmpty()) {
            return null;
        }

        Map<Integer, List<Genre>> genres = parameter.query(sqlSelectGenresToOneFilm, params, new GenreFromFilmMapper());
        setGenresFilms(film, genres);
        return film.get(0);
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = parameter.query(sqlSelectAllFilms, new FilmMapper());

        if (!films.isEmpty()) {
            Map<Integer, List<Genre>> genres = parameter.query(sqlSelectGenresAllFilms, new GenreFromFilmMapper());
            films = setGenresFilms(films, genres);
        }
        return films;
    }

    private List<Film> setGenresFilms(List<Film> films, Map<Integer, List<Genre>> genres) {
        if (genres != null) {
            return films.stream()
                    .map(film -> {
                        if (genres.containsKey(film.getId())) {
                            film.setGenres(genres.get(film.getId()));
                            return film;
                        }
                        return film;
                    }).collect(Collectors.toList());
        }
        return films;
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count) {
        Map<String, Object> params = Map.of("count", count);
        List<Film> films = parameter.query(sqlSelectPopularsFilms, params, new FilmMapper());

        if (!films.isEmpty()) {
            Map<Integer, List<Genre>> genres = parameter.query(sqlSelectPopularFilmsGenres, params,
                    new GenreFromFilmMapper());

            films = setGenresFilms(films, genres);
        }
        return films;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return parameter.query(sqlSelectAllGenes, new GenreMapper());
    }

    @Override
    public Genre getGenreById(Integer id) {
        return parameter.query(sqlSelectToOneGenre, Map.of("genreId", id), new GenreMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        return parameter.query(sqlSelectAllRatingMpa, new RatingMpaMapper());
    }

    @Override
    public RatingMpa getMpaById(Integer id) {
        return parameter.query(sqlSelectToOneRatingMpa, Map.of("ratingId", id), new RatingMpaMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert insertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> params = getFilmParams(film);
        film.setId(insertFilm.executeAndReturnKey(params).intValue());
        updateGenre(film);
        return getFilmsById(film.getId());
    }

    private Map<String, Object> getFilmParams(Film film) {
        return Map.of("name", film.getName(), "description", film.getDescription(),
                "release_date", film.getReleaseDate(), "duration", film.getDuration(),
                "rating_id", film.getMpa().getId(), "filmId", film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        Map<String, Object> params = getFilmParams(film);
        parameter.update(sqlUpdateFilm, params);
        parameter.update(sqlDeleteGenresFilm, Map.of("filmId", film.getId()));
        updateGenre(film);
        return getFilmsById(film.getId());
    }

    private void updateGenre(Film film) {
        if (!film.getGenres().isEmpty()) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            genres.forEach(genre -> parameter.update(sqlInsertGenresFilm,
                    Map.of("filmId", film.getId(), "genreId", genre.getId())));
        }
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        parameter.update(sqlInsertLikeFilm, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        parameter.update(sqlDeleteLikeFilm, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public void deleteFilm(Integer id) {
        parameter.update(sqlDeleteFilm, Map.of("filmId", id));
    }

    @Override
    public boolean isExistsIdFilm(Integer filmId) {
        List<Object> id = parameter.query(sqlSelectIdFilm, Map.of("filmId", filmId),
                (rs, rowNum) -> rs.getInt("film_id"));
        return id.size() == 1;
    }
}