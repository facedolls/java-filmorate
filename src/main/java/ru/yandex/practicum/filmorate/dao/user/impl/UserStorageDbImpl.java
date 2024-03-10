package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.user.UserStorageDb;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Primary
public class UserStorageDbImpl implements UserStorageDb {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "SELECT u.user_id, u.name, u.email, u.login, u.birthday, f.friend_id " +
                "FROM users AS u " +
                "LEFT JOIN friendship AS f ON u.user_id = f.user_id " +
                "WHERE u.user_id = :userId";
        return parameter.query(sqlQuery, Map.of("userId", id), new UserMapper()).stream()
                .findAny().orElse(null);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT u.user_id, u.name, u.email, u.login, u.birthday, f.friend_id " +
                "FROM users AS u " +
                "LEFT JOIN friendship AS f ON u.user_id = f.user_id " +
                "ORDER BY u.user_id";
        return parameter.query(sqlQuery, new UserMapper());
    }

    @Override
    public Collection<User> getFriends(Long id) {
        String sqlQuery = "SELECT u.user_id, u.name, u.email, u.login, u.birthday, f.friend_id " +
                "FROM users AS u " +
                "LEFT JOIN friendship AS f ON u.user_id = f.user_id " +
                "WHERE u.user_id IN " +
                "(SELECT friend_id FROM friendship " +
                "WHERE user_id = :userId AND status = true " +
                "GROUP BY friend_id) " +
                "ORDER BY u.user_id, u.name, u.email, u.login, u.birthday, f.friend_id";
        return parameter.query(sqlQuery, Map.of("userId", id), new UserMapper());
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        String sqlQuery = "SELECT u.user_id, u.name, u.email, u.login, u.birthday, f.friend_id " +
                "FROM users AS u " +
                "LEFT JOIN friendship AS f ON u.user_id = f.user_id " +
                "WHERE u.user_id IN " +
                "(SELECT friend_id FROM friendship " +
                "WHERE (user_id = :userId AND status = true) " +
                "OR (user_id = :otherId AND status = true) " +
                "GROUP BY friend_id " +
                "HAVING COUNT(friend_id) > 1)";
        return parameter.query(sqlQuery, Map.of("userId", id, "otherId", otherId), new UserMapper());
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
    public boolean addFriendRequest(Long id, Long friendId) {
        String sqlQueryFirst = "INSERT INTO friendship VALUES (:userId, :friendId, false)";
        parameter.update(sqlQueryFirst, Map.of("userId", id, "friendId", friendId));

        String sqlQuerySecond = "SELECT friend_id FROM friendship " +
                "WHERE user_id = :userId AND friend_id = :friendId AND status = false";
        List<Object> addedFriendId = parameter.query(sqlQuerySecond, Map.of("userId", id, "friendId", friendId),
                (rs, rowNum) -> rs.getInt("friend_id"));

        return addedFriendId.size() == 1;
    }

    @Override
    public User addInFriend(Long id, Long friendId) {
        String deleteFriendRequest = "DELETE FROM friendship WHERE user_id = :userId AND friend_id = :friendId";
        parameter.update(deleteFriendRequest, Map.of("userId", id, "friendId", friendId));

        String sqlQuery = "INSERT INTO friendship VALUES (:userId, :friendId, true)";
        parameter.update(sqlQuery, Map.of("userId", id, "friendId", friendId));
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = :email, login = :login, name = :name,  birthday = :birthday " +
                "WHERE user_id = :userId";
        Map<String, Object> params = Map.of("email", user.getEmail(), "login", user.getLogin(),
                "name", user.getName(), "birthday", user.getBirthday(), "userId", user.getId());
        parameter.update(sqlQuery, params);
        return getUserById(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = :userId";
        parameter.update(sqlQuery, Map.of("userId", id));
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = :userId AND friend_id = :friendId";
        parameter.update(sqlQuery, Map.of("userId", id, "friendId", friendId));
    }

    @Override
    public boolean isExistsIdUser(Long userId) {
        String sqlQuery = "SELECT user_id FROM users WHERE user_id = :userId";
        List<Object> id = parameter.query(sqlQuery, Map.of("userId", userId),
                (rs, rowNum) -> rs.getInt("user_id"));
        return id.size() == 1;
    }

    @Override
    public boolean isExistsFriendship(Long userId, Long friendId) {
        String sqlQuery = "SELECT user_id FROM friendship " +
                "WHERE user_id = :userId AND friend_id = :friendId AND status = true";
        List<Object> id = parameter.query(sqlQuery, Map.of("userId", userId, "friendId", friendId),
                (rs, rowNum) -> rs.getInt("user_id"));
        return id.size() == 1;
    }
}