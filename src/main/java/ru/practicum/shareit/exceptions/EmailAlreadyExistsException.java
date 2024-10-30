package ru.practicum.shareit.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE =
            "Указанная электронная почта уже существует, " +
                    "пользователь не был создан";

    public EmailAlreadyExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
