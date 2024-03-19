package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FriendsMapper implements ResultSetExtractor<Map<Long, Set<Long>>> {
    private final Map<Long, Set<Long>> friendsOfUsers = new HashMap<>();

    @Override
    public Map<Long, Set<Long>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            if (isUserFriendsInMap(rs)) {
                addToExistingSet(rs);
            } else {
                addNewSet(rs);
            }
        }
        return friendsOfUsers;
    }

    private boolean isUserFriendsInMap(ResultSet rs) throws SQLException {
        return friendsOfUsers.containsKey(rs.getLong("user_id"));
    }

    private void addToExistingSet(ResultSet rs) throws SQLException {
        Set<Long> friends = friendsOfUsers.get(rs.getLong("user_id"));
        friends.add(rs.getLong("friend_id"));
        friendsOfUsers.put(rs.getLong("user_id"), friends);
    }

    private void addNewSet(ResultSet rs) throws SQLException {
        Set<Long> friendsOfOneUser = new HashSet<>();
        friendsOfOneUser.add(rs.getLong("friend_id"));
        friendsOfUsers.put(rs.getLong("user_id"), friendsOfOneUser);
    }
}