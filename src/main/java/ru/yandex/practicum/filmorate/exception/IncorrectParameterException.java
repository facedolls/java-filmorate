package ru.yandex.practicum.filmorate.exception;

public class IncorrectParameterException extends NotFoundException {
    public IncorrectParameterException(final String message) {
        super(message);
    }
}
