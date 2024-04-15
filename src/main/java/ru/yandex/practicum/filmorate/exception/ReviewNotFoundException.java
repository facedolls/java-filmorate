package ru.yandex.practicum.filmorate.exception;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException(final String message) {
        super(message);
    }
}