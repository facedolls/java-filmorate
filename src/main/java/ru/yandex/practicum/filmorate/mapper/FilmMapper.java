package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FilmMapper implements RowMapper<Film> {
     @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new RatingMpa(rs.getInt("rating_id"), rs.getString("rating_name")),
                new ArrayList<>(), new ArrayList<>());
    }
}