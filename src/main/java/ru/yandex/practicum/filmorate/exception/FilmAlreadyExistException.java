package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyExistException extends RuntimeException {
    public FilmAlreadyExistException(final String message) {
        super(message);
    }
}
