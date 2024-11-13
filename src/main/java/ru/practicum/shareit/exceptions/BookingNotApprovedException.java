package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingNotApprovedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Невозможно оставить комментарий для неподтверждённого бронирования";

    public BookingNotApprovedException() {
        super(DEFAULT_MESSAGE);
    }

    public BookingNotApprovedException(String message) {
        super(message);
    }
}
