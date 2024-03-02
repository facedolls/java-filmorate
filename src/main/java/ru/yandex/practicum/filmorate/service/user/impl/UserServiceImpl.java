package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User getUserById(Long id) {
        checkUserExistence(id);
        return userStorage.getUsersById(id);
    }

    private void checkUserExistence(Long id) {
        if (userStorage.getUsersById(id) == null) {
            log.warn("User with id={} not found", id);
            throw new UserNotFoundException(String.format("User with id=%d not found", id));
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Received all users");
        return userStorage.getAllUsers();
    }

    @Override
    public Collection<User> getFriends(Long id) {
        Map<Long, Boolean> idFriends = getUserById(id).getFriends();
        log.info("Received a list of friends for user id={}", id);
        return idFriends.entrySet().stream()
                .filter(friend -> friend.getValue().equals(true))
                .map(Map.Entry::getKey)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        log.info("Received a list of mutual friends of user id={} and user otherId={}", id, otherId);
        return getUserById(id).getFriends().entrySet().stream()
                .filter(friend -> friend.getValue().equals(true))
                .map(Map.Entry::getKey)
                .filter(getUserById(otherId).getFriends().entrySet().stream()
                        .filter(friend -> friend.getValue().equals(true))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet())::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        setUserNameIfMissing(user);
        if (user.getId() != 0) {
            log.warn("User already exists {}", user);
            throw new UserAlreadyExistException("User already exists: " + user);
        }
        User createdUser = userStorage.createUser(user);
        log.info("Create user {}", user);
        return createdUser;
    }

    private void setUserNameIfMissing(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("The user's empty name {} has been changed to {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }

    @Override
    public User updateUser(User user) {
        setUserNameIfMissing(user);
        checkUserExistence(user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("Update user {}", user);
        return updatedUser;
    }

    @Override
    public String addInFriend(Long id, Long friendId) {
        User user = getUserById(id);
        user.getFriends().put(friendId, true);
        userStorage.updateUser(user);
        log.info("User id={} has been added as a friend to user id={} with status={}", friendId, id, true);
        return String.format(
                "User id=%d has been added as a friend to user id=%d with status=true", friendId, id);
    }

    @Override
    public String deleteForFriends(Long id, Long friendId) {
        User user= getUserById(id);
        user.getFriends().remove(friendId);
        userStorage.updateUser(user);
        log.info("User id={} has been removed from friends of user id={}", friendId, id);
        return String.format("User id=%d has been removed from friends of user id=%d", friendId, id);
    }

    @Override
    public String deleteUser(Long id) {
        checkUserExistence(id);
        userStorage.deleteUser(id);
        log.info("User id={} removed", id);
        return String.format("User id=%d deleted", id);
    }
}