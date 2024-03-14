package ru.yandex.practicum.filmorate.dao.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
public class FilmStorageDbIml implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    protected final String SqlSelectOneFilm = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "WHERE film_id = :filmId";
    protected final String SqlSelectGenresToOneFilm = "SELECT f.*, g.name " +
            "FROM film_genre AS f " +
            "JOIN genre AS g ON f.genre_id = g.genre_id " +
            "WHERE film_id = :filmId";
    protected final String SqlSelectLikesToOneFilm = "SELECT * FROM favorite_film WHERE film_id = :filmId";
    protected final String SqlSelectAllFilms = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "GROUP BY film_id";
    protected final String SqlSelectLikesAllFilms = "SELECT * FROM favorite_film";
    protected final String SqlSelectGenresAllFilms = "SELECT f.*, g.name " +
            "FROM film_genre AS f " +
            "JOIN genre AS g ON f.genre_id = g.genre_id";
    protected final String SqlSelectPopularsFilms = "SELECT f.*, r.name AS rating_name, " +
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
    protected final String SqlSelectAllGenes = "SELECT * FROM genre ORDER BY genre_id";
    protected final String SqlSelectAllRatingMpa = "SELECT * FROM rating ORDER BY rating_id";
    protected final String SqlSelectToOneGenre = "SELECT * FROM genre WHERE genre_id = :genreId";
    protected final String SqlSelectToOneRatingMpa = "SELECT * FROM rating WHERE rating_id = :ratingId";
    protected final String SqlUpdateFilm = "UPDATE film SET name = :name, description = :description, " +
            "release_date = :release_date, duration = :duration, rating_id = :rating_id " +
            "WHERE film_id = :filmId";
    protected final String SqlDeleteGenresFilm = "DELETE FROM film_genre WHERE film_id = :filmId";
    protected final String SqlInsertGenresFilm = "INSERT INTO film_genre VALUES (:filmId, :genreId)";
    protected final String SqlInsertLikeFilm = "INSERT INTO favorite_film VALUES (:filmId, :userId)";
    protected final String SqlDeleteLikeFilm = "DELETE FROM favorite_film " +
            "WHERE film_id = :filmId AND user_id = :userId";
    protected final String SqlDeleteFilm = "DELETE FROM film WHERE film_id = :filmId";
    protected final String SqlSelectIdFilm = "SELECT film_id FROM film WHERE film_id = :filmId";

    @Override
    public Film getFilmsById(Integer id) {
        Film film = parameter.query(SqlSelectOneFilm, Map.of("filmId", id), new FilmMapper()).stream()
                .findAny()
                .orElse(null);

        if (film != null) {
            setLikesToOneFilm(film);
            setGenresToOneFilm(film);
        }
        return film;
    }

    private void setLikesToOneFilm(Film film) {
        Map<Integer, Set<Long>> likes = parameter.query(SqlSelectLikesToOneFilm,
                Map.of("filmId", film.getId()), new LikeFromFilmMapper());

        if (likes != null) {
            film.setLike(likes.get(film.getId()));
        }
    }

    private void setGenresToOneFilm(Film film) {
        Map<Integer, List<Genre>> genres = parameter.query(SqlSelectGenresToOneFilm,
                Map.of("filmId", film.getId()), new GenreFromFilmMapper());

        if (genres != null) {
            film.setGenres(genres.get(film.getId()));
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = parameter.query(SqlSelectAllFilms, new FilmMapper());

        if (!films.isEmpty()) {
            films = setLikesAllFilms(films);
            films = setGenresAllFilms(films);
        }
        return films;
    }

    private List<Film> setLikesAllFilms(List<Film> films) {
        Map<Integer, Set<Long>> likes = parameter.query(SqlSelectLikesAllFilms, new LikeFromFilmMapper());
        if (likes != null) {
            return films.stream()
                    .map(film -> {
                        if (likes.containsKey(film.getId())) {
                            film.setLike(likes.get(film.getId()));
                            return film;
                        }
                        return film;
                    }).collect(Collectors.toList());
        }
        return films;
    }

    private List<Film> setGenresAllFilms(List<Film> films) {
        Map<Integer, List<Genre>> genres = parameter.query(SqlSelectGenresAllFilms, new GenreFromFilmMapper());
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
        List<Film> films = parameter.query(SqlSelectPopularsFilms, Map.of("count", count), new FilmMapper());

        if (!films.isEmpty()) {
            films = setLikesAllFilms(films);
            films = setGenresAllFilms(films);
        }
        return films;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return parameter.query(SqlSelectAllGenes, new GenreMapper());
    }

    @Override
    public Genre getGenreById(Integer id) {
        return parameter.query(SqlSelectToOneGenre, Map.of("genreId", id), new GenreMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<RatingMpa> getAllMpa() {
        return parameter.query(SqlSelectAllRatingMpa, new RatingMpaMapper());
    }

    @Override
    public RatingMpa getMpaById(Integer id) {
        return parameter.query(SqlSelectToOneRatingMpa, Map.of("ratingId", id), new RatingMpaMapper()).stream()
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
                "release_date", film.getReleaseDate().toString(), "duration", film.getDuration(),
                "rating_id", film.getMpa().getId(), "filmId", film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        Map<String, Object> params = getFilmParams(film);
        parameter.update(SqlUpdateFilm, params);
        parameter.update(SqlDeleteGenresFilm, Map.of("filmId", film.getId()));
        updateGenre(film);
        return getFilmsById(film.getId());
    }

    private void updateGenre(Film film) {
        if (!film.getGenres().isEmpty()) {
            film.getGenres().forEach(genre -> parameter.update(SqlInsertGenresFilm,
                            Map.of("filmId", film.getId(), "genreId", genre.getId())));
        }
    }

    @Override
    public Film putLike(Integer id, Long userId) {
        parameter.update(SqlInsertLikeFilm, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public Film deleteLike(Integer id, Long userId) {
        parameter.update(SqlDeleteLikeFilm, Map.of("filmId", id, "userId", userId));
        return getFilmsById(id);
    }

    @Override
    public void deleteFilm(Integer id) {
        parameter.update(SqlDeleteFilm, Map.of("filmId", id));
    }

    @Override
    public boolean isExistsIdFilm(Integer filmId) {
        List<Object> id = parameter.query(SqlSelectIdFilm, Map.of("filmId", filmId),
                (rs, rowNum) -> rs.getInt("film_id"));
        return id.size() == 1;
    }
}