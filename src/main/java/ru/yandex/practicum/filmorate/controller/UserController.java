package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.feedEvent.FeedEvent;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

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
    public List<FeedEvent> getFeedEventByUserId(@PathVariable @NotNull Long id) {
        return userService.getFeedEventByUserId(id);
    }

    @GetMapping("/{id}/recommendations")
    @ResponseBody
    public Collection<Film> getRecommendationsFilms(@PathVariable @NotNull Long id) {
        return userService.getRecommendationsFilms(id);
    }
}