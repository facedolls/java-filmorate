package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Director;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DirectorFromFilmMapper implements ResultSetExtractor<Map<Integer, List<Director>>> {
    private final Map<Integer, List<Director>> directorsOfFilms = new HashMap<>();

    @Override
    public Map<Integer, List<Director>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            if (isFilmDirectorsInMap(rs)) {
                addToExistingList(rs);
            } else {
                addNewList(rs);
            }
        }
        return directorsOfFilms;
    }

    private boolean isFilmDirectorsInMap(ResultSet rs) throws SQLException {
        return directorsOfFilms.containsKey(rs.getInt("film_id"));
    }

    private void addToExistingList(ResultSet rs) throws SQLException {
        List<Director> directors = directorsOfFilms.get(rs.getInt("film_id"));
        directors.add(new Director(rs.getInt("director_id"), rs.getString("name")));
        directorsOfFilms.put(rs.getInt("film_id"), directors);
    }

    private void addNewList(ResultSet rs) throws SQLException {
        List<Director> directorsOfOneFilm = new LinkedList<>();
        directorsOfOneFilm.add(new Director(rs.getInt("director_id"), rs.getString("name")));
        directorsOfFilms.put(rs.getInt("film_id"), directorsOfOneFilm);
    }
}