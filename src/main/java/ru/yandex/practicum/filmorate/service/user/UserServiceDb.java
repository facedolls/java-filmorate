package ru.yandex.practicum.filmorate.service.user;

public interface UserServiceDb extends UserService {
    String addFriendRequest(Long id, Long friendId);
}
