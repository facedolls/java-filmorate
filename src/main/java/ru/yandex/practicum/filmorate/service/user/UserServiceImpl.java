package ru.yandex.practicum.filmorate.service.user;

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
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User checkUserExistenceAndGetUserById(Long id) {
        if (id == null || id < 1) {
            log.warn("Incorrect id={} passed for user", id);
            throw new IncorrectParameterException(String.format("Error with field id=%d for user", id));
        }
        return userStorage.getAllUsers().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("User with id={} not found", id);
                    return new UserNotFoundException(String.format("User with id=%d not found", id));
                });
    }

    @Override
    public Collection<User> getAllUsers() {
        log.info("Received all users");
        return userStorage.getAllUsers();
    }

    @Override
    public Collection<User> getFriends(Long id) {
        Set<Long> idFriends = checkUserExistenceAndGetUserById(id).getFriends();
        log.info("Received a list of friends for user id={}", id);
        return idFriends.stream()
                .map(this::checkUserExistenceAndGetUserById)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        Set<Long> idCommonFriends = checkUserExistenceAndGetUserById(id).getFriends().stream()
                .filter(checkUserExistenceAndGetUserById(otherId).getFriends()::contains)
                .collect(Collectors.toSet());
        log.info("Received a list of mutual friends of user id={} and user otherId={}", id, otherId);
        return idCommonFriends.stream()
                .map(this::checkUserExistenceAndGetUserById)
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
        checkUserExistenceAndGetUserById(user.getId());
        User updatedUser = userStorage.updateUser(user);
        log.info("Update user {}", user);
        return updatedUser;
    }

    @Override
    public String addInFriend(Long id, Long friendId) {
        User userFirst = checkUserExistenceAndGetUserById(id);
        User userSecond = checkUserExistenceAndGetUserById(friendId);
        userFirst.getFriends().add(friendId);
        userSecond.getFriends().add(id);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("User id={} and user id={} are added as friends to each other", id, friendId);
        return String.format("User id=%d and user friendId=%d are added as friends to each other", id, friendId);
    }

    @Override
    public String deleteForFriends(Long id, Long friendId) {
        User userFirst = checkUserExistenceAndGetUserById(id);
        User userSecond = checkUserExistenceAndGetUserById(friendId);
        userFirst.getFriends().remove(friendId);
        userSecond.getFriends().remove(id);
        userStorage.updateUser(userFirst);
        userStorage.updateUser(userSecond);
        log.info("User id={} and user id={} are deleted as friends to each other", id, friendId);
        return String.format("User id=%d and user friendId=%d are deleted as friends to each other", id, friendId);
    }

    @Override
    public String deleteUser(Long id) {
        checkUserExistenceAndGetUserById(id);
        userStorage.deleteUser(id);
        log.info("User id={} removed", id);
        return String.format("User id=%d deleted", id);
    }
}