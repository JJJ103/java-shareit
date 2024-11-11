package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        booking.setItem(new Item());
        booking.getItem().setId(bookingDto.getItemId());

        booking.setBooker(new User());
        booking.getBooker().setId(bookingDto.getBookerId());

        booking.setStatus(bookingDto.getStatus() != null ? bookingDto.getStatus() : Booking.Status.WAITING);

        return booking;
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }
}
