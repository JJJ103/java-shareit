package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequestNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Запрос не найден";

    public RequestNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public RequestNotFoundException(String message) {
        super(message);
    }
}
