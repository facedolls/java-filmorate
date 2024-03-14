package ru.yandex.practicum.filmorate.dao.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.FriendsMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Primary
public class UserStorageDbImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcOperations parameter;
    protected final String SQL_SELECT_ONE_USER = "SELECT * FROM users WHERE user_id = :userId";
    protected final String SQL_SELECT_ID_FRIENDS_ONE_USER = "SELECT user_id, friend_id FROM friendship " +
            "WHERE user_id = :userId";
    protected final String SQL_SELECT_ALL_USERS = "SELECT * FROM users";
    protected final String SQL_SELECT_ID_FRIENDS_ALL_USERS = "SELECT user_id, friend_id FROM friendship";
    protected final String SQL_SELECT_FRIENDS_ONE_USER = "SELECT * FROM users " +
            "WHERE user_id IN " +
            "(SELECT friend_id FROM friendship " +
            "WHERE user_id = :userId)";
    protected final String SQL_SELECT_MUTUAL_FRIENDS = "SELECT * FROM users " +
            "WHERE user_id IN " +
            "(SELECT friend_id FROM friendship " +
            "WHERE user_id = :userId OR user_id = :otherId " +
            "GROUP BY friend_id " +
            "HAVING COUNT(friend_id) > 1)";
    protected final String SQL_INSERT_IN_FRIENDS = "INSERT INTO friendship VALUES (:userId, :friendId, true)";
    protected final String SQL_UPDATE_USER = "UPDATE users SET email = :email, " +
            "login = :login, name = :name,  birthday = :birthday " +
            "WHERE user_id = :userId";
    protected final String SQL_DELETE_USER = "DELETE FROM users WHERE user_id = :userId";
    protected final String SQL_DELETE_FROM_FRIENDS = "DELETE FROM friendship " +
            "WHERE user_id = :userId AND friend_id = :friendId";
    protected final String SQL_SELECT_ID_USER = "SELECT user_id FROM users WHERE user_id = :userId";
    protected final String SQL_SELECT_ID_USER_ELSE_FRIENDSHIP = "SELECT user_id FROM friendship " +
            "WHERE user_id = :userId AND friend_id = :friendId";

    @Override
    public User getUserById(Long id) {
         User user = parameter.query(SQL_SELECT_ONE_USER, Map.of("userId", id), new UserMapper()).stream()
                .findAny()
                .orElse(null);

        if (user != null) {
            setFriendsToOneUser(user);
        }
        return user;
    }

    private void setFriendsToOneUser(User user) {
        Map<Long, Set<Long>> friends = parameter.query(SQL_SELECT_ID_FRIENDS_ONE_USER,
                Map.of("userId", user.getId()), new FriendsMapper());

        if (friends != null) {
            user.setFriends(friends.get(user.getId()));
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        List<User> users = parameter.query(SQL_SELECT_ALL_USERS, new UserMapper());

        if (!users.isEmpty()) {
            return setFriendsAllUsers(users);
        }
        return users;
    }

    private List<User> setFriendsAllUsers(List<User> users) {
        Map<Long, Set<Long>> friends = parameter.query(SQL_SELECT_ID_FRIENDS_ALL_USERS, new FriendsMapper());
        if (friends != null) {
            return users.stream()
                    .map(user -> {
                        if (friends.containsKey(user.getId())) {
                            user.setFriends(friends.get(user.getId()));
                            return user;
                        }
                        return user;
                    }).collect(Collectors.toList());
        }
        return users;
    }

    @Override
    public Collection<User> getFriends(Long id) {
        List<User> friendsUser = parameter.query(SQL_SELECT_FRIENDS_ONE_USER,
                Map.of("userId", id), new UserMapper());

        if (!friendsUser.isEmpty()) {
            return setFriendsAllUsers(friendsUser);
        }
        return friendsUser;
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        List<User> mutualFriends = parameter.query(SQL_SELECT_MUTUAL_FRIENDS,
                Map.of("userId", id, "otherId", otherId), new UserMapper());

        if (!mutualFriends.isEmpty()) {
            return setFriendsAllUsers(mutualFriends);
        }
        return mutualFriends;
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
        parameter.update(SQL_INSERT_IN_FRIENDS, Map.of("userId", id, "friendId", friendId));
        return getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        parameter.update(SQL_UPDATE_USER, getUserParams(user));
        return getUserById(user.getId());
    }

    private Map<String, Object> getUserParams(User user) {
        return Map.of("email", user.getEmail(), "login", user.getLogin(), "name", user.getName(),
                "birthday", user.getBirthday(), "userId", user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        parameter.update(SQL_DELETE_USER, Map.of("userId", id));
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        parameter.update(SQL_DELETE_FROM_FRIENDS, Map.of("userId", id, "friendId", friendId));
    }

    @Override
    public boolean isExistsIdUser(Long userId) {
        List<Integer> id = parameter.query(
                SQL_SELECT_ID_USER, Map.of("userId", userId),
                (rs, rowNum) -> rs.getInt("user_id"));

        return id.size() == 1;
    }

    @Override
    public boolean isExistsFriendship(Long userId, Long friendId) {
       List<Integer> id = parameter.query(SQL_SELECT_ID_USER_ELSE_FRIENDSHIP,
               Map.of("userId", userId, "friendId", friendId),
               (rs, rowNum) -> rs.getInt("user_id"));

        return id.size() == 1;
    }
}