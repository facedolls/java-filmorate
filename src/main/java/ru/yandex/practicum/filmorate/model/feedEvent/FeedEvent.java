package ru.yandex.practicum.filmorate.model.feedEvent;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class FeedEvent {
    private Long eventId;
    private Long timestamp;
    private Long userId;
    private EventType eventType;
    private EventOperation operation;
    private Long entityId;
}