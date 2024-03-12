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

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());

            user.setFriends(returnFriends(rs));
            users.add(user);
            user = new User();
        }
        return users;
    }

    private Set<Long> returnFriends(ResultSet rs) throws SQLException {
        Set<Long> friends = new HashSet<>();
        if (rs.getString("friends").contains("[null]")) {
            return friends;
        }

        String[] allFriends = rs.getString("friends")
                .replaceAll("[^A-Za-zА-Яа-я0-9,]", "")
                .split(",");
        Arrays.stream(allFriends).forEach(f -> friends.add(Long.parseLong(f)));
        return friends;
    }
}