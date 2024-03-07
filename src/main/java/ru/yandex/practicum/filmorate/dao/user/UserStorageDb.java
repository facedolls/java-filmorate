package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

public interface UserStorageDb extends UserStorage {
    Collection<User> getFriends(Long id);

    Collection<User> getMutualFriends(Long id, Long otherId);

    void addFriendRequest(Long id, Long friendId);

    void addInFriend(Long id, Long friendId);

    void deleteForFriends(Long id, Long friendId);
}
