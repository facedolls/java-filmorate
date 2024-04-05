package ru.yandex.practicum.filmorate.dao.feedEvent;

import ru.yandex.practicum.filmorate.model.feedEvent.FeedEvent;

import java.util.List;

public interface FeedEventStorage {

    List<FeedEvent> getFeedEventByUserId(long userId);

    void addFeedEvent(FeedEvent event);
}