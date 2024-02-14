package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final String message) {
        super(message);
    }
}
