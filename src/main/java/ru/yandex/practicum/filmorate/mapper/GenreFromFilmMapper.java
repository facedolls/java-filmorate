package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenreFromFilmMapper implements ResultSetExtractor<Map<Integer, List<Genre>>> {
    private final Map<Integer, List<Genre>> genresOfFilms = new HashMap<>();

    @Override
    public Map<Integer, List<Genre>> extractData(ResultSet rs) throws DataAccessException, SQLException {
        while (rs.next()) {
            if (isFilmGenresInMap(rs)) {
                addToExistingList(rs);
            } else {
                addNewList(rs);
            }
        }
        return genresOfFilms;
    }

    private boolean isFilmGenresInMap(ResultSet rs) throws SQLException {
        return genresOfFilms.containsKey(rs.getInt("film_id"));
    }

    private void addToExistingList(ResultSet rs) throws SQLException {
        List<Genre> genres = genresOfFilms.get(rs.getInt("film_id"));
        genres.add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
        genresOfFilms.put(rs.getInt("film_id"), genres);
    }

    private void addNewList(ResultSet rs) throws SQLException {
        List<Genre> genresOfOneFilm = new LinkedList<>();
        genresOfOneFilm.add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
        genresOfFilms.put(rs.getInt("film_id"), genresOfOneFilm);
    }
}