package ru.yandex.practicum.filmorate.service.feedEvent;

import ru.yandex.practicum.filmorate.model.feedEvent.*;
import java.util.List;

public interface FeedEventService {
    List<FeedEvent> getFeedEventByUserId(Long userId);

    void addFeedEvent(Long userId, EventType eventType, EventOperation eventOperation, Long entityId);
}