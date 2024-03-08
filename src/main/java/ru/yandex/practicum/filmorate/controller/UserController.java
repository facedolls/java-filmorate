package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceDb;
import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserServiceDb userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable @NotNull @Min(1) Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getFriends(@PathVariable @NotNull @Min(1) Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getMutualFriends(@PathVariable @NotNull @Min(1) Long id,
                                             @PathVariable @NotNull @Min(1) Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public String addFriendRequest(@PathVariable @NotNull @Min(1) Long id,
                                   @PathVariable @NotNull @Min(1) Long friendId) {
        return userService.addFriendRequest(id, friendId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addInFriend(@PathVariable @NotNull @Min(1) Long id,
                              @PathVariable @NotNull @Min(1) Long friendId) {
        return userService.addInFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteForFriends(@PathVariable @NotNull @Min(1) Long id,
                                   @PathVariable @NotNull @Min(1) Long friendId) {
        return userService.deleteForFriends(id, friendId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(@PathVariable @NotNull @Min(1) Long id) {
        return userService.deleteUser(id);
    }
}