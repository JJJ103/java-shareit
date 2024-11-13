package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemRequestNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Запрос на предмет не найден";

    public ItemRequestNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}
