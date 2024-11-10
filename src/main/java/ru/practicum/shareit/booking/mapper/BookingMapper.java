package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class BookingMapper {

    // Преобразует BookingDto в объект Booking
    public static Booking toBooking(BookingDto bookingDto) {
        Booking booking = new Booking();

        // Устанавливаем начальные и конечные временные метки из DTO
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        // Устанавливаем предмет из DTO по itemId
        booking.setItem(new Item());
        booking.getItem().setId(bookingDto.getItemId());

        // Устанавливаем пользователя из DTO по bookerId
        booking.setBooker(new User());
        booking.getBooker().setId(bookingDto.getBookerId());

        // Устанавливаем статус из DTO (если он есть)
        booking.setStatus(bookingDto.getStatus() != null ? bookingDto.getStatus() : Booking.Status.WAITING);

        return booking;
    }

    // Преобразует объект Booking в BookingDto
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        // Устанавливаем данные из объекта Booking в DTO
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItemId(booking.getItem().getId());
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }
}
