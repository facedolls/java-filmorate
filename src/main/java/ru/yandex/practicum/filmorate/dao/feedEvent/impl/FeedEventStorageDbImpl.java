package ru.yandex.practicum.filmorate.dao.feedEvent.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.feedEvent.FeedEventStorage;
import ru.yandex.practicum.filmorate.mapper.FeedEventMapper;
import ru.yandex.practicum.filmorate.model.feedEvent.FeedEvent;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedEventStorageDbImpl implements FeedEventStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FeedEventMapper feedEventMapper;

    @Override
    public List<FeedEvent> getFeedEventByUserId(Long userId) {
        final String sql = "SELECT event_id, timestamp, user_id, event_type, operation, entity_id " +
                "FROM feed " +
                "WHERE user_id = ? ";
        return jdbcTemplate.query(sql, feedEventMapper, userId);

    }

    @Override
    public void addFeedEvent(FeedEvent feedEvent) {
        final String sql = "INSERT INTO feed " +
                "(timestamp, user_id, event_type, operation, entity_id) values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                feedEvent.getTimestamp(),
                feedEvent.getUserId(),
                feedEvent.getEventType().name(),
                feedEvent.getOperation().name(),
                feedEvent.getEntityId()
        );
    }
}