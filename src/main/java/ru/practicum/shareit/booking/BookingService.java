package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    Booking createBooking(Long userId, BookingDto bookingDto);

    Booking confirmBooking(Long bookingId, Long userId, Boolean approved);

    Booking getBooking(Long bookingId, Long userId);

    List<Booking> getAllBookings(Long userId, String state);

    List<Booking> getBookingsForOwner(Long userId, String state);
}
