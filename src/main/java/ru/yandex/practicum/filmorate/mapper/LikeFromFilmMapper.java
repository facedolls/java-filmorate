package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.*;
import java.util.*;

public class LikeFromFilmMapper implements ResultSetExtractor<Map<Integer, Set<Long>>> {
    private final Map<Integer, Set<Long>> likesOfFilms = new HashMap<>();

    @Override
    public Map<Integer, Set<Long>> extractData(ResultSet rs) throws DataAccessException, SQLException {
        while (rs.next()) {
            if (isFilmLikesInMap(rs)) {
                addToExistingSet(rs);
            } else {
                addNewSet(rs);
            }
        }
        return likesOfFilms;
    }

    private boolean isFilmLikesInMap(ResultSet rs) throws SQLException {
        return likesOfFilms.containsKey(rs.getInt("film_id"));
    }

    private void addToExistingSet(ResultSet rs) throws SQLException {
        Set<Long> likes = likesOfFilms.get(rs.getInt("film_id"));
        likes.add(rs.getLong("user_id"));
        likesOfFilms.put(rs.getInt("film_id"), likes);
    }

    private void addNewSet(ResultSet rs) throws SQLException {
        Set<Long> likesOfOneFilm = new HashSet<>();
        likesOfOneFilm.add(rs.getLong("user_id"));
        likesOfFilms.put(rs.getInt("film_id"), likesOfOneFilm);
    }
}