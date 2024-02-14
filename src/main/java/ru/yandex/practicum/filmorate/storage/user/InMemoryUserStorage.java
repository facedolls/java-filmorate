package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

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
}