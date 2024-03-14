package ru.yandex.practicum.filmorate.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FriendsMapper implements ResultSetExtractor<Map<Long, Set<Long>>> {
    private final Map<Long, Set<Long>> friendsUser = new HashMap<>();
    private Set<Long> friends = new HashSet<>();
    private Long userId = 0L;

    @Override
    public Map<Long, Set<Long>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            if (isAllFriendsUser(rs)) {
                friendsUser.put(userId, friends);
                friends = new HashSet<>();
            }
            userId = rs.getLong("user_id");
            friends.add(rs.getLong("friend_id"));
        }

        friendsUser.put(userId, friends);
        return friendsUser;
    }

    private boolean isAllFriendsUser(ResultSet rs) throws SQLException {
        return rs.getLong("user_id") != userId && userId != 0L;
    }
}
