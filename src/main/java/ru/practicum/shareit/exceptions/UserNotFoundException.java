package ru.practicum.shareit.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Пользователь не найден";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
