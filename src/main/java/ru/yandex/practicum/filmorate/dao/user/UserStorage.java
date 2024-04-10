package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.*;
import java.util.*;

public interface UserStorage {
    User getUserById(Long id);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    Collection<User> getFriends(Long id);

    Collection<User> getMutualFriends(Long id, Long otherId);

    User addInFriend(Long id, Long friendId);

    void deleteFromFriends(Long id, Long friendId);

    boolean isExistsIdUser(Long userId);

    boolean isExistsFriendship(Long id, Long friendId);

    List<Film> getRecommendationsFilms(Long id);
}