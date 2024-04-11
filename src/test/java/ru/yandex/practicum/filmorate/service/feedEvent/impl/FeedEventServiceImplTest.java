package ru.yandex.practicum.filmorate.service.feedEvent.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.feedEvent.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.yandex.practicum.filmorate.model.feedEvent.EventOperation.*;
import static ru.yandex.practicum.filmorate.model.feedEvent.EventType.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = "/test-data-feedEvent.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FeedEventServiceImplTest {

    /*@Autowired
    private final UserService userService;

    @Autowired
    private final FilmService filmService;

    @Test
    void addAndDeleteLikesTest() {
        filmService.putLike(1L, 1L);
        List<FeedEvent> feedEvents1 = userService.getFeedEventByUserId(1L);
        FeedEvent feedEvent = feedEvents1.get(0);
        assertFieldsOfFeedEvent(feedEvent, 1L, 1L, LIKE, ADD, 1L);

        filmService.deleteLike(1L, 1L);
        List<FeedEvent> feedEvents2 = userService.getFeedEventByUserId(1L);
        assertThat(feedEvents2.size()).isEqualTo(2);

        FeedEvent feedEvent1 = feedEvents2.get(0);
        FeedEvent feedEvent2 = feedEvents2.get(1);
        assertFieldsOfFeedEvent(feedEvent1, 1L, 1L, LIKE, ADD, 1L);
        assertFieldsOfFeedEvent(feedEvent2, 2L, 1L, LIKE, REMOVE, 1L);
    }

    @Test
    void addAndDeleteFriendFeedEventTest() {
        userService.addInFriend(1L, 2L);
        List<FeedEvent> feedEvents1 = userService.getFeedEventByUserId(1L);
        assertThat(feedEvents1.size()).isEqualTo(1);

        FeedEvent feedEvent = feedEvents1.get(0);
        assertFieldsOfFeedEvent(feedEvent, 1L, 1L, FRIEND, ADD, 2L);

        userService.deleteFromFriends(1L, 2L);
        List<FeedEvent> feedEvents2 = userService.getFeedEventByUserId(1L);
        assertThat(feedEvents2.size()).isEqualTo(2);

        FeedEvent feedEvent1 = feedEvents2.get(0);
        FeedEvent feedEvent2 = feedEvents2.get(1);
        assertFieldsOfFeedEvent(feedEvent1, 1L, 1L, FRIEND, ADD, 2L);
        assertFieldsOfFeedEvent(feedEvent2, 2L, 1L, FRIEND, REMOVE, 2L);
    }

    @Test
    void emptyFeedEventTest() {
        List<FeedEvent> feedEvent = userService.getFeedEventByUserId(1L);
        assertThat(feedEvent.isEmpty()).isTrue();
    }

    @Test
    void addLikeAndFriendTest() {
        userService.addInFriend(1L, 2L);
        filmService.putLike(1L, 1L);
        List<FeedEvent> feedEvents1 = userService.getFeedEventByUserId(1L);
        assertThat(feedEvents1.size()).isEqualTo(2);

        FeedEvent feedEvent1 = feedEvents1.get(0);
        FeedEvent feedEvent2 = feedEvents1.get(1);
        assertFieldsOfFeedEvent(feedEvent1, 1L, 1L, FRIEND, ADD, 2L);
        assertFieldsOfFeedEvent(feedEvent2, 2L, 1L, LIKE, ADD, 1L);
    }

    private static void assertFieldsOfFeedEvent(FeedEvent feedEvent, Long eventId, Long userId, EventType type,
                                                EventOperation operation, Long entityId) {
        assertThat(feedEvent).hasFieldOrPropertyWithValue("eventId", eventId);
        assertThat(feedEvent).hasFieldOrPropertyWithValue("userId", userId);
        assertThat(feedEvent).hasFieldOrPropertyWithValue("eventType", type);
        assertThat(feedEvent).hasFieldOrPropertyWithValue("operation", operation);
        assertThat(feedEvent).hasFieldOrPropertyWithValue("entityId", entityId);
    }*/
}