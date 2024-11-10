package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Бронирование не найдено>";

    public BookingNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BookingNotFoundException(String message) {
        super(message);
    }
}
