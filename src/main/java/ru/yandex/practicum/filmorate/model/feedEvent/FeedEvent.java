package ru.yandex.practicum.filmorate.model.feedEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedEvent {
    private Long eventId;
    private Long timestamp;
    private Long userId;
    private EventType eventType;
    private EventOperation operation;
    private Long entityId;
}
