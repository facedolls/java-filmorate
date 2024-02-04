package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(final String message) {
        super(message);
    }
}
