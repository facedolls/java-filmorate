package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Primary
public class UserStorageDbImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
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
}