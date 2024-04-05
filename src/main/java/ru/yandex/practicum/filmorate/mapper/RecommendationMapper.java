package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecommendationMapper implements ResultSetExtractor<List<Film>> {
    private final List<Film> films = new ArrayList<>();
    private Film film = new Film();
    private List<Genre> genres = new ArrayList<>();
    private List<Director> directors = new ArrayList<>();

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            if (isFullFilm(rs)) {
                film.setDirectors(directors);
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

            if (isDirectorOfThisFilm(rs)) {
                directors.add(new Director(rs.getInt("director_id"), rs.getString("director_name")));
            }

            if (isGenreOfThisFilm(rs)) {
                genres.add(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            }
        }

        if (isLastFilm(film)) {
            film.setDirectors(directors);
            film.setGenres(genres);
            films.add(film);
        }

        return films;
    }

    private boolean isGenreOfThisFilm(ResultSet rs) throws SQLException {
        return rs.getInt("genre_id") != 0 &&
                !genres.contains(new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
    }

    private boolean isDirectorOfThisFilm(ResultSet rs) throws SQLException {
        return rs.getInt("director_id") != 0 &&
                !directors.contains(new Director(rs.getInt("director_id"), rs.getString("director_name")));
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
        directors = new ArrayList<>();
    }
}
