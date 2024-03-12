package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User getUserById(Long id) {
        checkUserExistence(id);
        return userStorage.getUserById(id);
    }

    private void checkUserExistence(Long id) {
        if (userStorage.getUserById(id) == null) {
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
        getUserById(id);
        log.info("Received a list of friends for user id={}", id);
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        getUserById(id);
        getUserById(otherId);
        log.info("Received a list of mutual friends of user id={} and user otherId={}", id, otherId);
        return userStorage.getMutualFriends(id, otherId);
    }

    @Override
    public User createUser(User user) {
        setUserNameIfMissing(user);
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
        checkUserExistence(user.getId());
        setUserNameIfMissing(user);
        User updatedUser = userStorage.updateUser(user);
        log.info("Update user {}", user);
        return updatedUser;
    }

    @Override
    public User addInFriend(Long id, Long friendId) {
        checkUserExistence(id);
        checkUserExistence(friendId);
        log.info("User id={} and user id={} are added as friends to each other", id, friendId);
        return userStorage.addInFriend(id, friendId);
    }

    @Override
    public String deleteFromFriends(Long id, Long friendId) {
        checkUserExistence(id);
        checkUserExistence(friendId);
        userStorage.isExistsFriendship(id, friendId);
        userStorage.deleteFromFriends(id, friendId);
        log.info("User id={} and user id={} are deleted as friends to each other", id, friendId);
        return String.format("User id=%d and user friendId=%d are deleted as friends to each other", id, friendId);
    }

    @Override
    public String deleteUser(Long id) {
        checkUserExistence(id);
        userStorage.deleteUser(id);
        log.info("User id={} removed", id);
        return String.format("User id=%d deleted", id);
    }
}