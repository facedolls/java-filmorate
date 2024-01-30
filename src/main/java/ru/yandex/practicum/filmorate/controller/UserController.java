package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static int id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        setUserNameIfMissing(user);
        if (user.getId() != null) {
            log.warn("An exception \"UserAlreadyExistException\" was thrown for {}", user);
            throw new UserAlreadyExistException("User already exists: " + user);
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Create user: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        setUserNameIfMissing(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Update user: {}", user);
        } else {
            log.warn("An exception \"ValidationException\" was thrown for {}", user);
            throw new ValidationException("Incorrect id passed for: " + user);
        }
        return user;
    }

    private void setUserNameIfMissing(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("The user's empty name {} has been changed to {}", user, user.getLogin());
            user.setName(user.getLogin());
        }
    }
}