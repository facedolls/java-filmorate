package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyExistException extends ValidationException {
    public FilmAlreadyExistException(final String message) {
        super(message);
    }
}
