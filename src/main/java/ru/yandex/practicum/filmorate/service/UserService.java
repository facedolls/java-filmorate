package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private static long id = 1;
    private final UserStorage userStorage;

    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (id == null || id < 1) {
            log.warn("Incorrect id={} passed for user", id);
            throw new IncorrectParameterException(String.format("Error with field id=%d for user", id));
        }
        if (user == null) {
            log.warn("User id={} not found", id);
            throw new UserNotFoundException(String.format("User with id=%d not found", id));
        }
        log.info("User received by id={}", id);
        return user;
    }

    public Collection<User> getAllUsers() {
        log.info("Received all users");
        return userStorage.getAllUsers();
    }

    public Collection<User> getFriends(Long id) {
        Set<Long> idFriends = getUserById(id).getFriends();
        log.info("Received a list of friends for user id={}", id);
        return idFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriends(Long id, Long otherId) {
        Set<Long> idCommonFriends = getUserById(id).getFriends().stream()
                .filter(getUserById(otherId).getFriends()::contains)
                .collect(Collectors.toSet());
        log.info("Received a list of mutual friends of user id={} and user otherId={}", id, otherId);
        return idCommonFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        setUserNameIfMissing(user);
        if (user.getId() != 0) {
            log.warn("User already exists {}", user);
            throw new UserAlreadyExistException("User already exists: " + user);
        }
        user.setId(id++);
        User createdUser = userStorage.createUser(user);
        log.info("Create user {}", user);
        return createdUser;
    }

    public User updateUser(User user) {
        setUserNameIfMissing(user);
        getUserById(user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("Update user {}", user);
        return updatedUser;
    }

    public String addInFriend(Long id, Long friendId) {
        User userFirst = getUserById(id);
        User userSecond = getUserById(friendId);
        userFirst.getFriends().add(friendId);
        userSecond.getFriends().add(id);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("User id={} and user id={} are added as friends to each other", id, friendId);
        return String.format("User id=%d and user friendId=%d are added as friends to each other", id, friendId);
    }

    public String deleteForFriends(Long id, Long friendId) {
        User userFirst = getUserById(id);
        User userSecond = getUserById(friendId);
        userFirst.getFriends().remove(friendId);
        userSecond.getFriends().remove(id);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("User id={} and user id={} are deleted as friends to each other", id, friendId);
        return String.format("User id=%d and user friendId=%d are deleted as friends to each other", id, friendId);
    }

    public String deleteUser(Long id) {
        getUserById(id);
        userStorage.deleteUser(id);
        log.info("User id={} removed", id);
        return String.format("User id=%d deleted", id);
    }

    private void setUserNameIfMissing(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("The user's empty name {} has been changed to {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }
}