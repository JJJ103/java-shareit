package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingUnavailableException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Предмет не доступен для бронирования";

    public BookingUnavailableException() {
        super(DEFAULT_MESSAGE);
    }

    public BookingUnavailableException(String message) {
        super(message);
    }
}