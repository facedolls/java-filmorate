package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id));
    }
    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<Collection<User>> getFriends(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Collection<User>> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getMutualFriends(id, otherId));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> addInFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.addInFriend(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> deleteForFriends(@PathVariable Long id, @PathVariable Long friendId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteForFriends(id, friendId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(id));
    }
}