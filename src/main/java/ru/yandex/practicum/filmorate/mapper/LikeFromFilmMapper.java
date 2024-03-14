package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.*;
import java.util.*;

public class LikeFromFilmMapper implements ResultSetExtractor<Map<Integer, Set<Long>>> {
    private final Map<Integer, Set<Long>> filmsLikes = new HashMap<>();
    private Set<Long> likes = new HashSet<>();
    private Integer filmId = 0;

    @Override
    public Map<Integer, Set<Long>> extractData(ResultSet rs) throws DataAccessException, SQLException {
        while (rs.next()) {
            if (isAllLikesFilm(rs)) {
                filmsLikes.put(filmId, likes);
                likes = new HashSet<>();
            }
            filmId = rs.getInt("film_id");
            likes.add(rs.getLong("user_id"));
        }

        if (!likes.isEmpty()) {
            filmsLikes.put(filmId, likes);
        }
        return filmsLikes;
    }

    private boolean isAllLikesFilm(ResultSet rs) throws SQLException {
        return rs.getInt("film_id") != filmId && filmId != 0;
    }
}