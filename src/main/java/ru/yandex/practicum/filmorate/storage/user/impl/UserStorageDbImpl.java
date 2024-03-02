package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorageDb;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserStorageDbImpl implements UserStorageDb {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUsersById(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from \"user\" where id = ?", id);

        if(userRows.next()) {
            User user = new User(
                    userRows.getInt("user_id"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());

            log.info("Найден пользователь: id={}, name={}, email={}", user.getId(), user.getName(), user.getEmail());
            return user;
        }
            log.info("Пользователь с идентификатором {} не найден.", id);
            return null;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sqlQuery = "select * from \"user\"";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUsers);
    }

    private User mapRowToUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt("user_id"),
                resultSet.getString("email"),
                resultSet.getString("login"),
                resultSet.getString("name"),
                resultSet.getDate("birthday").toLocalDate());
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public Collection<User> getFriends(Long id) {
        return null;
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        return null;
    }

    @Override
    public String addInFriend(Long id, Long friendId) {
        return null;
    }

    @Override
    public String deleteForFriends(Long id, Long friendId) {
        return null;
    }
}