package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.user.UserStorageDb;
import ru.yandex.practicum.filmorate.service.user.UserServiceDb;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class UserServiceDbImpl implements UserServiceDb {
    private final UserStorageDb userStorage;

    @Override
    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.warn("User with id={} not found", id);
            throw new UserNotFoundException(String.format("User with id=%d not found", id));
        }
        return user;
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

    private void isExistsIdUser(Long userId) {
        boolean isExists = userStorage.isExistsIdUser(userId);
        if (!isExists) {
            log.warn("User with id={} not found", userId);
            throw new UserNotFoundException(String.format("User with id=%d not found", userId));
        }
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
        setUserNameIfMissing(user);
        if (user.getId() != 0) {
            log.warn("User already exists {}", user);
            throw new UserAlreadyExistException("User already exists: " + user);
        }
        User userCreated = userStorage.createUser(user);
        log.info("Create user {}", userCreated);
        return userCreated;
    }

    private void setUserNameIfMissing(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("The user's empty name {} has been changed to {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }

    @Override
    public String addFriendRequest(Long id, Long friendId) {
        isExistsIdUser(id);
        isExistsIdUser(friendId);
        boolean isAddFriendRequest = userStorage.addFriendRequest(id, friendId);

        if (!isAddFriendRequest) {
            log.info("Not added friend request to user id={} from user id={}", friendId, id);
            return String.format(
                    "Not added friend request to user id=%d from user id=%d", friendId, id);
        }

        log.info("Added a friend request to user id={} from user id={} with status={}", friendId, id, false);
        return String.format(
                "Added a friend request to user id=%d from user id=%d with status=false", friendId, id);
    }

    @Override
    public User updateUser(User user) {
        isExistsIdUser(user.getId());
        log.info("Update user {}", user);
        return userStorage.updateUser(user);
    }

    @Override
    public User addInFriend(Long id, Long friendId) {
        isExistsIdUser(id);
        isExistsIdUser(friendId);
        User user = userStorage.addInFriend(id, friendId);
        log.info("User id={} has been added as a friend to user id={} with status={}", friendId, id, true);
        return user;
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
}