package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.feedEvent.FeedEvent;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable @NotNull @Min(1) Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable @NotNull @Min(1) Long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable @NotNull @Min(1) Long id,
                                             @PathVariable @NotNull @Min(1) Long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        if (user.getId() != 0) {
            log.warn("Incorrect id={} was passed when creating the user: ", user.getId());
            throw new ValidationException("id for the user must not be specified");
        }
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addInFriend(@PathVariable @NotNull @Min(1) Long id,
                            @PathVariable @NotNull @Min(1) Long friendId) {
        return userService.addInFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteForFriends(@PathVariable @NotNull @Min(1) Long id,
                                   @PathVariable @NotNull @Min(1) Long friendId) {
        return userService.deleteFromFriends(id, friendId);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable @NotNull @Min(1) Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}/feed")
    public List<FeedEvent> getFeedEventByUserId(@PathVariable Long id) {
        return userService.getFeedEventByUserId(id);
    }

    @GetMapping("/{id}/recommendations")
    @ResponseBody
    public List<Film> getRecommendationsFilms(@PathVariable Long id) {
        log.info("Получение списка рекомендаций по фильмам для пользователя ");

        return userService.getRecommendationsFilms(id);
    }
}
