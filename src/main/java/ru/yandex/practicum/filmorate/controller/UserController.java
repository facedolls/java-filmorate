package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
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
        validationUser(user);
        generateId(user);
        if (!users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Added user: {}", user);
        } else {
            log.warn("An exception \"UserAlreadyExistException\" was thrown for {}", user);
            throw new UserAlreadyExistException("User already exists: " + user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validationUser(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Update user: {}", user);
        } else {
            log.warn("An exception \"ValidationException\" was thrown for {}", user);
            throw new ValidationException("Incorrect id passed for: " + user);
        }
        return user;
    }

    private void validationUser(User user) {
        boolean isNotLogin = user.getLogin().contains(" ");
        boolean isNotBirthday = user.getBirthday().isAfter(LocalDate.now());

        if (isNotLogin && isNotBirthday) {
            log.warn("An exception \"ValidationException\" was thrown for {}", user);
            throw new ValidationException("The user is not validated: " + user);
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("The user's empty name {} has been changed to {}", user, user.getName());
        }
    }

    private void generateId(User user) {
        if (user.getId() == 0) {
            user.setId(++id);
        } else {
            id = user.getId();
        }
    }
}