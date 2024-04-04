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
            "JOIN genre AS g ON f.genre_id = g.genre_id " +
            "ORDER BY g.genre_id";
    protected final String sqlSelectPopularsFilms = "SELECT f.*, r.name AS rating_name, " +
            "COUNT(l.user_id) AS count_likes " +
            "FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id, rating_name " +
            "ORDER BY COUNT(l.user_id) DESC " +
            "LIMIT :count";
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
            "ORDER BY COUNT(ff.film_id) DESC, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.genre_id, g.name";
    protected final String sqlSelectDirectorsToOneFilm = "SELECT f.*, d.name " +
            "FROM film_director AS f " +
            "JOIN director AS d ON f.director_id = d.director_id " +
            "WHERE film_id = :filmId";
    protected final String sqlSelectDirectorsAllFilms = "SELECT f.*, d.name " +
            "FROM film_director AS f " +
            "JOIN director AS d ON f.director_id = d.director_id";
    protected final String sqlSelectPopularFilmsDirectors = "SELECT f.*, d.name, COUNT(user_id) as count_likes " +
            "FROM film_director AS f " +
            "LEFT JOIN director AS d ON f.director_id = d.director_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(ff.film_id) desc, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.director_id, d.name";
    protected final String sqlInsertDirectorsFilm = "INSERT INTO film_director VALUES (:filmId, :directorId)";
    protected final String sqlSelectAllDirectors = "SELECT * FROM director";
    protected final String sqlSelectToOneDirector = "SELECT * FROM director WHERE director_id = :directorId";
    protected final String sqlUpdateDirector = "UPDATE director SET name = :name WHERE director_id = :directorId";
    protected final String sqlDeleteDirector = "DELETE FROM director WHERE director_id = :directorId";
    protected final String sqlDeleteDirectorsFilm = "DELETE FROM film_director WHERE film_id = :filmId";
    protected final String sqlSelectFilmsByDirectorAndLike = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "JOIN film_director AS fd ON f.film_id = fd.film_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE director_id = :directorId " +
            "GROUP BY f.film_id, rating_name " +
            "ORDER BY COUNT(ff.film_id) DESC, f.film_id";
    protected final String sqlSelectFilmsByDirectorAndYear = "SELECT f.*, r.name AS rating_name FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "JOIN film_director AS fd ON f.film_id = fd.film_id " +
            "WHERE director_id = :directorId " +
            "GROUP BY f.film_id, rating_name " +
            "ORDER BY release_date";
    protected final String sqlSelectFilmsByDirectorDirectors = "SELECT f.*, d.name FROM film_director AS f " +
            "JOIN director AS d ON f.director_id = d.director_id " +
            "WHERE d.director_id = :directorId";
    protected final String sqlSelectFilmsByDirectorGenres = "SELECT fg.*, g.name FROM film_genre AS fg " +
            "JOIN genre AS g ON fg.genre_id = g.genre_id " +
            "JOIN film_director AS ff ON fg.film_id = ff.film_id " +
            "JOIN director AS d ON d.director_id = ff.director_id " +
            "WHERE d.director_id = :directorId";
    protected final String sqlSelectTopFilmsByYear = "SELECT f.*, r.name AS rating_name, " +
            "COUNT(l.user_id) AS count_likes " +
            "FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
            "WHERE EXTRACT(YEAR FROM release_date) = :year " +
            "GROUP BY f.film_id, rating_name " +
            "ORDER BY COUNT(l.user_id) DESC " +
            "LIMIT :count";
    protected final String sqlSelectGenresTopFilmsByYear = "SELECT f.*, g.name, COUNT(user_id) AS count_likes " +
            "FROM film_genre AS f " +
            "LEFT JOIN genre AS g ON f.genre_id = g.genre_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "WHERE EXTRACT(YEAR FROM release_date) = :year " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(ff.film_id) DESC, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.genre_id, g.name " +
            "ORDER BY f.genre_id";
    protected final String sqlSelectDirectorsTopFilmsByYear = "SELECT f.*, d.name, COUNT(user_id) AS count_likes " +
            "FROM film_director AS f " +
            "LEFT JOIN director AS d ON f.director_id = d.director_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "WHERE EXTRACT(YEAR FROM release_date) = :year " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(ff.film_id) DESC, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.director_id, d.name " +
            "ORDER by f.director_id";
    protected final String sqlSelectTopFilmsByYearAndGenre = "SELECT f.*, r.name AS rating_name, " +
            "COUNT(l.user_id) AS count_likes " +
            "FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
            "JOIN film_genre AS g ON f.film_id = g.film_id " +
            "WHERE g.genre_id = :genreId AND EXTRACT(YEAR FROM release_date) = :year " +
            "GROUP BY f.film_id, rating_name " +
            "ORDER BY COUNT(f.film_id) DESC " +
            "LIMIT :count";
    protected final String sqlSelectGenresTopFilmsByYearAndGenre = "SELECT f.*, g.name, COUNT(user_id) AS count_likes " +
            "FROM film_genre AS f " +
            "LEFT JOIN genre AS g ON f.genre_id = g.genre_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "JOIN film_genre AS fg ON fm.film_id = fg.film_id " +
            "WHERE fg.genre_id = :genreId AND EXTRACT(YEAR FROM release_date) = :year " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(ff.film_id) DESC, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.genre_id, g.name";
    protected final String sqlSelectDirectorsTopFilmsByYearAndGenre = "SELECT f.*, d.name, " +
            "COUNT(user_id) AS count_likes " +
            "FROM film_director AS f " +
            "LEFT JOIN director AS d ON f.director_id = d.director_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "JOIN film_genre AS fg ON fm.film_id = fg.film_id " +
            "WHERE fg.genre_id = :genreId AND EXTRACT(YEAR FROM release_date) = :year " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(ff.film_id) DESC, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.director_id, d.name";
    protected final String sqlSelectTopFilmsByGenre = "SELECT f.*, r.name AS rating_name, " +
            "COUNT(l.user_id) AS count_likes " +
            "FROM film AS f " +
            "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
            "LEFT JOIN favorite_film AS l ON f.film_id = l.film_id " +
            "JOIN film_genre AS g ON f.film_id = g.film_id " +
            "WHERE g.genre_id = :genreId " +
            "GROUP BY f.film_id, rating_name " +
            "ORDER BY COUNT(f.film_id) DESC " +
            "LIMIT :count";
    protected final String sqlSelectGenresTopFilmsByGenre = "SELECT f.*, g.name, COUNT(user_id) AS count_likes " +
            "FROM film_genre AS f " +
            "LEFT JOIN genre AS g ON f.genre_id = g.genre_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON fm.film_id = ff.film_id " +
            "JOIN film_genre AS fg ON fm.film_id = fg.film_id " +
            "WHERE fg.genre_id = :genreId " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(ff.film_id) DESC, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.genre_id, g.name";
    protected final String sqlSelectDirectorsTopFilmsByGenre = "SELECT f.*, d.name, COUNT(user_id) AS count_likes " +
            "FROM film_director AS f " +
            "LEFT JOIN director AS d ON f.director_id = d.director_id " +
            "LEFT JOIN favorite_film AS ff ON f.film_id = ff.film_id " +
            "WHERE f.film_id IN (" +
            "SELECT fm.film_id FROM film AS fm " +
            "LEFT JOIN favorite_film AS ff ON ff.film_id = fm.film_id " +
            "LEFT JOIN film_genre AS fg ON fg.film_id = fm.film_id " +
            "WHERE fg.genre_id = :genreId " +
            "GROUP BY fm.film_id " +
            "ORDER BY COUNT(ff.film_id) DESC, fm.film_id " +
            "LIMIT :count) " +
            "GROUP BY f.film_id, f.director_id, d.name " +
            "ORDER BY f.director_id";

    @Override
    public Film getFilmsById(Integer id) {
        Map<String, Object> params = Map.of("filmId", id);
        List<Film> film = parameter.query(sqlSelectOneFilm, params, new FilmMapper());

        if (film.isEmpty()) {
            return null;
        }
        film = setFilmsWithGenresAndDirectors(film, params, sqlSelectGenresToOneFilm, sqlSelectDirectorsToOneFilm);
        return film.get(0);
    }

    private List<Film> setFilmsWithGenresAndDirectors(List<Film> films, Map<String, Object> params,
                                                         String sqlGenres, String sqlDirectors) {
        Map<Integer, List<Genre>> genres = parameter.query(sqlGenres, params, new GenreFromFilmMapper());
        films = setGenresFilms(films, genres);

        Map<Integer, List<Director>> directors = parameter.query(sqlDirectors, params, new DirectorFromFilmMapper());
        films = setDirectorsFilms(films, directors);
        return films;
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> films = parameter.query(sqlSelectAllFilms, new FilmMapper());

        if (!films.isEmpty()) {
            Map<Integer, List<Genre>> genres = parameter.query(sqlSelectGenresAllFilms, new GenreFromFilmMapper());
            films = setGenresFilms(films, genres);

            Map<Integer, List<Director>> directors = parameter.query(sqlSelectDirectorsAllFilms,
                    new DirectorFromFilmMapper());
            films = setDirectorsFilms(films, directors);
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

    private List<Film> setDirectorsFilms(List<Film> films, Map<Integer, List<Director>> directors) {
        if (directors != null) {
            return films.stream()
                    .map(film -> {
                        if (directors.containsKey(film.getId())) {
                            film.setDirectors(directors.get(film.getId()));
                            return film;
                        }
                        return film;
                    }).collect(Collectors.toList());
        }
        return films;
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        if (genreId == 0 && year == 0) {
            return sortTopFilmsByGenresOrYear(Map.of("count", count), sqlSelectPopularsFilms,
                    sqlSelectPopularFilmsGenres, sqlSelectPopularFilmsDirectors);
        } else if (genreId != 0 && year != 0) {
            return sortTopFilmsByGenresOrYear(Map.of("count", count, "genreId", genreId,"year", year),
                    sqlSelectTopFilmsByYearAndGenre, sqlSelectGenresTopFilmsByYearAndGenre,
                    sqlSelectDirectorsTopFilmsByYearAndGenre);
        } else if (genreId != 0) {
            return sortTopFilmsByGenresOrYear(Map.of("count", count, "genreId", genreId),
                    sqlSelectTopFilmsByGenre, sqlSelectGenresTopFilmsByGenre, sqlSelectDirectorsTopFilmsByGenre);
        }
        return sortTopFilmsByGenresOrYear(Map.of("count", count, "year", year), sqlSelectTopFilmsByYear,
                sqlSelectGenresTopFilmsByYear, sqlSelectDirectorsTopFilmsByYear);
    }

    private Collection<Film> sortTopFilmsByGenresOrYear(Map<String, Object> params, String sqlFilms,
                                                            String sqlGenres, String sqlDirectors) {
        List<Film> films = parameter.query(sqlFilms, params, new FilmMapper());
        if (!films.isEmpty()) {
            return setFilmsWithGenresAndDirectors(films, params, sqlGenres, sqlDirectors);
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
        updateDirectorFromFilm(film);
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
        parameter.update(sqlDeleteDirectorsFilm, Map.of("filmId", film.getId()));

        updateGenre(film);
        updateDirectorFromFilm(film);
        return getFilmsById(film.getId());
    }

    private void updateGenre(Film film) {
        if (!film.getGenres().isEmpty()) {
            Set<Genre> genres = new HashSet<>(film.getGenres());
            genres.forEach(genre -> parameter.update(sqlInsertGenresFilm,
                    Map.of("filmId", film.getId(), "genreId", genre.getId())));
        }
    }

    private void updateDirectorFromFilm(Film film) {
        if (!film.getDirectors().isEmpty()) {
            Set<Director> directors = new HashSet<>(film.getDirectors());
            directors.forEach(director -> parameter.update(sqlInsertDirectorsFilm,
                    Map.of("filmId", film.getId(), "directorId", director.getId())));
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

    @Override
    public Collection<Film> getFilmsByDirector(Integer directorId, String sortBy) {
        Map<String, Object> params = Map.of("directorId", directorId);
        if (sortBy.equals("likes")) {
            return sortFilmsByDirectorAndArgument(params, sqlSelectFilmsByDirectorAndLike);
        }
        return sortFilmsByDirectorAndArgument(params, sqlSelectFilmsByDirectorAndYear);
    }

    private Collection<Film> sortFilmsByDirectorAndArgument(Map<String, Object> params, String sqlFilms) {
        List<Film> films = parameter.query(sqlFilms, params, new FilmMapper());
        if (!films.isEmpty()) {
            return setFilmsWithGenresAndDirectors(films, params,
                    sqlSelectFilmsByDirectorGenres, sqlSelectFilmsByDirectorDirectors);
        }
        return films;
    }

    @Override
    public Collection<Director> getAllDirectors() {
        return parameter.query(sqlSelectAllDirectors, new DirectorMapper());
    }

    @Override
    public Director getDirectorById(Integer id) {
        return parameter.query(sqlSelectToOneDirector, Map.of("directorId", id), new DirectorMapper()).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Director createDirector(Director director) {
        SimpleJdbcInsert insertDirector = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");

        director.setId(insertDirector.executeAndReturnKey(Map.of("name", director.getName())).intValue());
        return getDirectorById(director.getId());
    }

    @Override
    public Director updateDirector(Director director) {
        Map<String, Object> params = Map.of("name", director.getName(), "directorId", director.getId());
        parameter.update(sqlUpdateDirector, params);
        return getDirectorById(director.getId());
    }

    @Override
    public void deleteDirector(Integer id) {
        parameter.update(sqlDeleteDirector, Map.of("directorId", id));
    }
}