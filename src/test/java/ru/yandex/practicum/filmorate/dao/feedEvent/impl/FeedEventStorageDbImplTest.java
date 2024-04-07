package ru.yandex.practicum.filmorate.dao.feedEvent.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.feedEvent.FeedEventStorage;
import ru.yandex.practicum.filmorate.model.feedEvent.FeedEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.filmorate.model.feedEvent.EventOperation.ADD;
import static ru.yandex.practicum.filmorate.model.feedEvent.EventType.FRIEND;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = "/feedEvent-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FeedEventStorageDbImplTest {
    @Autowired
    private FeedEventStorage feedEventStorage;

    @Test
    public void getFeedEventByUserId() {
        assertEquals(3, feedEventStorage.getFeedEventByUserId(1L).size());
        assertEquals(1, feedEventStorage.getFeedEventByUserId(2L).size());
    }

    @Test
    public void addFeedEvent() {
        FeedEvent feedEvent1 = new FeedEvent(2L, 2L, 2L, FRIEND, ADD, 1L);
        feedEventStorage.addFeedEvent(feedEvent1);
        feedEventStorage.getFeedEventByUserId(2L);
        assertEquals(2, feedEventStorage.getFeedEventByUserId(2L).size());
    }
}