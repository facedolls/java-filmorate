package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorageDb;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceDbImpl implements UserService {
    private final UserStorageDb userStorage;

    @Override
    public User getUserById(Long id) {
        return userStorage.getUsersById(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public Collection<User> getFriends(Long id) {
        return userStorage.getFriends(id);
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        return userStorage.getMutualFriends(id, otherId);
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public String addInFriend(Long id, Long friendId) {
        return userStorage.addInFriend(id, friendId);
    }

    @Override
    public String deleteForFriends(Long id, Long friendId) {
        return userStorage.deleteForFriends(id, friendId);
    }

    @Override
    public String deleteUser(Long id) {
        userStorage.deleteUser(id);
        log.info("User with id={} deleted", id);
        return String.format("User with id=%d deleted", id);
    }
}