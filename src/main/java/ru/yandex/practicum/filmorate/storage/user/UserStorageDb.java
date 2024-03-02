package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserStorageDb extends UserStorage {
    Collection<User> getFriends(Long id);

    Collection<User> getMutualFriends(Long id, Long otherId);

    String addInFriend(Long id, Long friendId);

    String deleteForFriends(Long id, Long friendId);
}
