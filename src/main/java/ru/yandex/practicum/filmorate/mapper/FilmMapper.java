package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmMapper implements ResultSetExtractor<List<Film>> {
    private final List<Film> films = new ArrayList<>();
    private Film film = new Film();
    private List<Genre> genres = new ArrayList<>();
    private Set<Long> likes = new HashSet<>();

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            if (isFullFilm(rs)) {
                film.setLike(likes);
                film.setGenres(genres);
                films.add(film);
                prepareForNewFilm();
            }

            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setMpa(new RatingMpa(rs.getInt("rating_id"), rs.getString("rating_name")));

            if (isLikeOfThisFilm(rs)) {
                likes.add(rs.getLong("user_id"));
            }

            if (isGenreOfThisFilm(rs, genres)) {
                genres.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            }
        }

        if (isLastFilm(film)) {
            film.setLike(likes);
            film.setGenres(genres);
            films.add(film);
        }

        return films;
    }

    private boolean isGenreOfThisFilm(ResultSet rs, List<Genre> genres) throws SQLException {
        return rs.getInt("genre_id") != 0 &&
                !genres.contains(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }

    private boolean isLikeOfThisFilm(ResultSet rs) throws SQLException {
        return rs.getLong("user_id") != 0L;
    }

    private boolean isFullFilm(ResultSet rs) throws SQLException {
        return rs.getInt("film_id") != film.getId() && film.getId() != 0;
    }

    private boolean isLastFilm(Film film) {
        return film.getId() != 0;
    }

    private void prepareForNewFilm() {
        film = new Film();
        genres = new ArrayList<>();
        likes = new HashSet<>();
    }
}