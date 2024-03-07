package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.query(sqlQuery, new UserMapper(), id)
                .stream().findFirst().orElse(null);
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, new UserMapper());
    }

    @Override
    public Collection<User> getFriends(Long id) {
        String sqlQuery = "SELECT * FROM users " +
                "WHERE user_id IN " +
                "(SELECT friend_id FROM friendship " +
                "WHERE user_id = ? AND status = true " +
                "GROUP BY friend_id) " +
                "ORDER BY user_id";
        return jdbcTemplate.query(sqlQuery, new UserMapper(), id);
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        String sqlQuery = "SELECT * FROM users " +
                "WHERE user_id IN " +
                "(SELECT friend_id FROM friendship " +
                "WHERE (user_id = ? AND status = true) " +
                "OR (user_id = ? AND status = true) " +
                "GROUP BY friend_id " +
                "HAVING COUNT(friend_id) > 1)";
        return jdbcTemplate.query(sqlQuery, new UserMapper(), id, otherId);
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(user);
        user.setId(simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue());
        return user;
    }

    @Override
    public void addFriendRequest(Long id, Long friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, false)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void addInFriend(Long id, Long friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, true)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?,  birthday = ? WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void deleteForFriends(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }
}