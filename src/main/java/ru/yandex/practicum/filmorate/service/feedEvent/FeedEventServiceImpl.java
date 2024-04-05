package ru.yandex.practicum.filmorate.service.feedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.feedEvent.FeedEventStorage;
import ru.yandex.practicum.filmorate.model.feedEvent.EventOperation;
import ru.yandex.practicum.filmorate.model.feedEvent.EventType;
import ru.yandex.practicum.filmorate.model.feedEvent.FeedEvent;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class FeedEventServiceImpl implements FeedEventService {

    private final FeedEventStorage feedEventStorage;

    @Override
    public List<FeedEvent> getFeedEventByUserId(long userId) {
        log.info("user event feed with userId = {}", userId);
        return feedEventStorage.getFeedEventByUserId(userId);
    }

    @Override
    public void addFeedEvent(long userId, EventType eventType, EventOperation eventOperation, long entityId) {
        FeedEvent feedEvent = FeedEvent.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(eventOperation)
                .entityId(entityId)
                .build();

        log.info("an event has been added to the feed = {}", feedEvent);
        feedEventStorage.addFeedEvent(feedEvent);
    }
}