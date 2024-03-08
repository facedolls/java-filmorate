package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserMapper implements ResultSetExtractor<List<User>> {
    private final List<User> users = new ArrayList<>();
    private User user = new User();
    private Set<Long> friends = new HashSet<>();

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            if (isFullUser(rs)) {
                user.setFriends(friends);
                users.add(user);
                prepareForNewUser();
            }

            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());

            if (isFriendOfThisUser(rs)) {
                friends.add(rs.getLong("friend_id"));
            }
        }

        if (isLastUser(user)) {
            user.setFriends(friends);
            users.add(user);
        }

        return users;
    }

    private boolean isFriendOfThisUser(ResultSet rs) throws SQLException {
        return rs.getLong("friend_id") != 0L;
    }

    private boolean isFullUser(ResultSet rs) throws SQLException {
        return rs.getInt("user_id") != user.getId() && user.getId() != 0;
    }

    private boolean isLastUser(User user) {
        return user.getId() != 0;
    }

    private void prepareForNewUser() {
        user = new User();
        friends = new HashSet<>();
    }
}
