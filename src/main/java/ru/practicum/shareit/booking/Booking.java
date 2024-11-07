package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status = Status.WAITING; // Статус бронирования, установлено значение по умолчанию

    public enum Status {
        WAITING, APPROVED, REJECTED, CANCELED
    }
}
