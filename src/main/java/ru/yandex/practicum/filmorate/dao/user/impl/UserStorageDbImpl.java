package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.mapper.RecommendationMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Primary
@Slf4j
public class UserStorageDbImpl implements UserStorage {
    protected final String sqlSelectOneUser = "SELECT * FROM users WHERE user_id = :userId";
    protected final String sqlSelectAllUser = "SELECT * FROM users";
    protected final String sqlSelectFriendsOneUser = "SELECT * FROM users " +
            "WHERE user_id IN " +
            "(SELECT friend_id FROM friendship " +
            "WHERE user_id = :userId)";
    protected final String sqlSelectMutualFriends = "SELECT * FROM users " +
            "WHERE user_id IN " +
            "(SELECT friend_id FROM friendship " +
            "WHERE user_id = :userId OR user_id = :otherId " +
            "GROUP BY friend_id " +
            "HAVING COUNT(friend_id) > 1)";
    protected final String sqlInsertInFriends = "INSERT INTO friendship VALUES (:userId, :friendId, true)";
    protected final String sqlUpdateUser = "UPDATE users SET email = :email, " +
            "login = :login, name = :name,  birthday = :birthday " +
            "WHERE user_id = :userId";
    protected final String sqlDeleteUser = "DELETE FROM users WHERE user_id = :userId";
    protected final String sqlDeleteFromFriends = "DELETE FROM friendship " +
            "WHERE user_id = :userId AND friend_id = :friendId";
    protected final String sqlSelectIdUser = "SELECT user_id FROM users WHERE user_id = :userId";
    protected final String sqlSelectIdUserElseFriendship = "SELECT user_id FROM friendship " +
            "WHERE user_id = :userId AND friend_id = :friendId";
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;

    @Override
    public User getUserById(Long id) {
        Map<String, Object> params = Map.of("userId", id);
        List<User> user = parameter.query(sqlSelectOneUser, params, new UserMapper());

        if (!user.isEmpty()) {
            return user.get(0);
        }
        return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        return parameter.query(sqlSelectAllUser, new UserMapper());
    }

    @Override
    public Collection<User> getFriends(Long id) {
        Map<String, Object> params = Map.of("userId", id);
        return parameter.query(sqlSelectFriendsOneUser, params, new UserMapper());
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        Map<String, Object> params = Map.of("userId", id, "otherId", otherId);
        return parameter.query(sqlSelectMutualFriends, params, new UserMapper());
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(user);
        user.setId(simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue());
        return getUserById(user.getId());
    }

    @Override
    public User addInFriend(Long id, Long friendId) {
        parameter.update(sqlInsertInFriends, Map.of("userId", id, "friendId", friendId));
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        parameter.update(sqlUpdateUser, getUserParams(user));
        return getUserById(user.getId());
    }

    private Map<String, Object> getUserParams(User user) {
        return Map.of("email", user.getEmail(), "login", user.getLogin(), "name", user.getName(),
                "birthday", user.getBirthday(), "userId", user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        parameter.update(sqlDeleteUser, Map.of("userId", id));
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        parameter.update(sqlDeleteFromFriends, Map.of("userId", id, "friendId", friendId));
    }

    @Override
    public boolean isExistsIdUser(Long userId) {
        List<Integer> id = parameter.query(
                sqlSelectIdUser, Map.of("userId", userId),
                (rs, rowNum) -> rs.getInt("user_id"));

        return id.size() == 1;
    }

    @Override
    public boolean isExistsFriendship(Long userId, Long friendId) {
        List<Integer> id = parameter.query(sqlSelectIdUserElseFriendship,
                Map.of("userId", userId, "friendId", friendId),
                (rs, rowNum) -> rs.getInt("user_id"));

        return id.size() == 1;
    }

    @Override
    public List<Film> getRecommendationsFilms(Long id) {

        if (id == null) {
            throw new IncorrectParameterException("Null id ");
        }

        log.info("Запрос к db по user: {} ", id);

        List<String> recommendationsFilmsId = jdbcTemplate.queryForList(
                "SELECT DISTINCT film_id FROM favorite_film " +
                        "JOIN (SELECT second_user.user_id, " +
                        "COUNT(second_user.film_id) AS count_films " +
                        "FROM (SELECT * FROM favorite_film " +
                        "WHERE user_id != ?) AS second_user " +
                        "JOIN (SELECT * FROM favorite_film " +
                        "WHERE user_id = ?) AS base_user " +
                        "ON base_user.film_id = second_user.film_id " +
                        "GROUP BY second_user.user_id " +
                        "ORDER BY count_films DESC " +
                        "LIMIT (SELECT CEILING(COUNT(user_id) * 0.1) " +
                        "FROM favorite_film " +
                        "WHERE film_id IN (SELECT film_id FROM favorite_film " +
                        "WHERE user_id = ?))) AS user_top " +
                        "ON favorite_film.user_id = user_top.user_id " +
                        "WHERE favorite_film.film_id not IN (SELECT film_id FROM favorite_film " +
                        "WHERE user_id = ?)", String.class, id, id, id, id);

        if (recommendationsFilmsId.isEmpty()) {
            return new ArrayList<>();
        }

        String filmsRow = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rating_id, " +
                "m.name AS rating_name, fd.director_id, " +
                "di.name AS director_name, fg.genre_id, g.name AS genre_name " +
                "FROM film AS f " +
                "JOIN rating AS m ON f.rating_id = m.rating_id " +
                "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN director AS di ON fd.director_id = di.director_id " +
                "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id IN (?) " +
                "ORDER BY fd.director_id, fg.genre_id";
        return jdbcTemplate.query(filmsRow, new RecommendationMapper(), String.join(",", recommendationsFilmsId));
    }
}
