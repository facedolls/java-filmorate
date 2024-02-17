package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistException extends ValidationException  {
    public UserAlreadyExistException(final String message) {
        super(message);
    }
}
