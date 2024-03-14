package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GenreFromFilmMapper implements ResultSetExtractor<Map<Integer, List<Genre>>> {
    private final Map<Integer, List<Genre>> filmsGenres = new HashMap<>();
    private List<Genre> genres = new ArrayList<>();
    private Integer filmId = 0;

    @Override
    public Map<Integer, List<Genre>> extractData(ResultSet rs) throws DataAccessException, SQLException {

    while (rs.next()) {
        if (isAllGenresFilm(rs)) {
            filmsGenres.put(filmId, genres);
            genres = new ArrayList<>();
        }
        filmId = rs.getInt("film_id");
        genres.add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
    }

    if (!genres.isEmpty()) {
        filmsGenres.put(filmId, genres);
    }
        return filmsGenres;
}

    private boolean isAllGenresFilm(ResultSet rs) throws SQLException {
        return rs.getInt("film_id") != filmId && filmId != 0;
    }
}
