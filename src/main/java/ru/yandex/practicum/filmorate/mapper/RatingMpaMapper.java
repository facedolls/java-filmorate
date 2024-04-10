package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import java.sql.*;

public class RatingMpaMapper implements RowMapper<RatingMpa> {
    @Override
    public RatingMpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RatingMpa(rs.getInt("rating_id"),
                rs.getString("name"));
    }
}