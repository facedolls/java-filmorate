package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.feedEvent.EventOperation;
import ru.yandex.practicum.filmorate.model.feedEvent.EventType;
import ru.yandex.practicum.filmorate.model.feedEvent.FeedEvent;
import ru.yandex.practicum.filmorate.service.feedEvent.FeedEventService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final FeedEventService feedEventService;

    @Override
    public User getUserById(Long id) {
        isExistsIdUser(id);
        log.info("Received user id={}", id);
        return userStorage.getUserById(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Received all users");
        return userStorage.getAllUsers();
    }

    @Override
    public Collection<User> getFriends(Long id) {
        isExistsIdUser(id);
        log.info("Received a list of friends for user id={}", id);
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        isExistsIdUser(id);
        isExistsIdUser(otherId);
        log.info("Received a list of mutual friends of user id={} and user otherId={}", id, otherId);
        return userStorage.getMutualFriends(id, otherId);
    }

    @Override
    public User createUser(User user) {
        if (user.getId() != null) {
            log.warn("Incorrect id={} was passed when creating the user: ", user.getId());
            throw new ValidationException("id for the user must not be specified");
        }

        setUserNameIfMissing(user);
        log.info("Create user {}", user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        isExistsIdUser(user.getId());
        setUserNameIfMissing(user);
        log.info("Update user {}", user);
        return userStorage.updateUser(user);
    }

    @Override
    public User addInFriend(Long id, Long friendId) {
        isExistsIdUser(id);
        isExistsIdUser(friendId);
        feedEventService.addFeedEvent(id, EventType.FRIEND, EventOperation.ADD, friendId);
        log.info("User id={} has been added as a friend to user id={} with status={}", friendId, id, true);
        return userStorage.addInFriend(id, friendId);
    }

    @Override
    public String deleteFromFriends(Long id, Long friendId) {
        isExistsIdUser(id);
        isExistsIdUser(friendId);
        boolean isExistsFriendship = userStorage.isExistsFriendship(id, friendId);
        if (!isExistsFriendship) {
            log.info("Friendship user id={} with user id={} not found", friendId, id);
            return String.format("Friendship user id=%d with user id=%d not found", friendId, id);
        }
        userStorage.deleteFromFriends(id, friendId);
        feedEventService.addFeedEvent(id, EventType.FRIEND, EventOperation.REMOVE, friendId);
        log.info("User id={} has been removed from friends of user id={}", friendId, id);
        return String.format("User id=%d has been removed from friends of user id=%d", friendId, id);
    }

    @Override
    public String deleteUser(Long id) {
        isExistsIdUser(id);
        userStorage.deleteUser(id);
        log.info("User with id={} deleted", id);
        return String.format("User with id=%d deleted", id);
    }

    @Override
    public void isExistsIdUser(Long userId) {
        boolean isExists = userStorage.isExistsIdUser(userId);
        if (!isExists) {
            log.warn("User with id={} not found", userId);
            throw new UserNotFoundException(String.format("User with id=%d not found", userId));
        }
    }

    @Override
    public List<FeedEvent> getFeedEventByUserId(Long userId) {
        getUserById(userId);
        return feedEventService.getFeedEventByUserId(userId);
    }

    @Override
    public List<Film> getRecommendationsFilms(Long id) {
        log.info("Запрос к db по user: {} ", id);
        return userStorage.getRecommendationsFilms(id);
    }

    private void setUserNameIfMissing(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("The user's empty name {} has been changed to {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }
}