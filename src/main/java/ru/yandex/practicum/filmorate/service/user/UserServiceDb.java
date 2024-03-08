package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

public interface UserServiceDb extends UserService {
    String addFriendRequest(Long id, Long friendId);
}
