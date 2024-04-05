package ru.yandex.practicum.filmorate.model.feedEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedEvent {
    long eventId;
    long timestamp;
    long userId;
    EventType eventType;
    EventOperation operation;
    long entityId;
}