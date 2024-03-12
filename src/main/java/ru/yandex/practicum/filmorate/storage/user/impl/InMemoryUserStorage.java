package ru.yandex.practicum.filmorate.storage.user.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static long id = 1;

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public Collection<User> getFriends(Long id) {
        return getUserById(id)
                .getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getMutualFriends(Long id, Long otherId) {
        Set<Long> idCommonFriends = getUserById(id).getFriends().stream()
                .filter(getUserById(otherId).getFriends()::contains)
                .collect(Collectors.toSet());

        return idCommonFriends.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User addInFriend(Long id, Long friendId) {
        User user = getUserById(id);
        user.getFriends().add(friendId);
        return updateUser(user);
    }

    @Override
    public void deleteFromFriends(Long id, Long friendId) {
        User user = getUserById(id);
        user.getFriends().remove(friendId);
        updateUser(user);
    }

    @Override
    public boolean isExistsIdUser(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean isExistsFriendship(Long id, Long friendId) {
        Set<Long> friendsUserFirst = users.get(id).getFriends();
        Set<Long> friendsUserSecond = users.get(friendId).getFriends();
        return friendsUserFirst.contains(friendId) && friendsUserSecond.contains(id);
    }
}