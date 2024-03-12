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

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setMpa(new RatingMpa(rs.getInt("rating_id"), rs.getString("rating_name")));

            film.setLike(returnLikes(rs));
            film.setGenres(returnGenres(rs));
            films.add(film);
            film = new Film();
        }
        return films;
    }

    private Set<Long> returnLikes(ResultSet rs) throws SQLException {
        Set<Long> likes = new HashSet<>();
        if (rs.getString("likes").contains("[null]")) {
            return likes;
        }

        String[] allLikes = rs.getString("likes")
                .replaceAll("[^A-Za-zА-Яа-я0-9,]", "")
                .split(",");
        Arrays.stream(allLikes).forEach(l -> likes.add(Long.parseLong(l)));
        return likes;
    }

    private List<Genre> returnGenres(ResultSet rs) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        if (rs.getString("genres").contains("[null]")) {
            return genres;
        }

        String[] allGenre = (rs.getString("genres")
                .replaceAll("[^A-Za-zА-Яа-я0-9,]", "")
                .split(","));
        for (int i = 0; i < allGenre.length; i = i + 2) {
            genres.add(new Genre(Integer.parseInt(allGenre[i]), allGenre[i + 1]));
        }
        return genres;
    }
}