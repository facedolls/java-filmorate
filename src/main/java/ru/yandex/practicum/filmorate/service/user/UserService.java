package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;

public interface UserService {
    User getUserById(Long id);

    Collection<User> getAllUsers();

    Collection<User> getFriends(Long id);

    Collection<User> getMutualFriends(Long id, Long otherId);

    User createUser(User user);

    User updateUser(User user);

    User addInFriend(Long id, Long friendId);

    String deleteFromFriends(Long id, Long friendId);

    String deleteUser(Long id);

    void isExistsIdUser(Long userId);
}