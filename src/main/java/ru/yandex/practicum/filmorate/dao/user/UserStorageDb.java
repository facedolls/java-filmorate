package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;

public interface UserStorageDb extends UserStorage {
    Collection<User> getFriends(Long id);

    Collection<User> getMutualFriends(Long id, Long otherId);

    boolean addFriendRequest(Long id, Long friendId);

    User addInFriend(Long id, Long friendId);

    void deleteFromFriends(Long id, Long friendId);

    boolean isExistsIdUser(Long userId);

    boolean isExistsFriendship(Long id, Long friendId);
}
