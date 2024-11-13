package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

public class BookingResponseMapper {

    public static BookingResponseDTO toBookingResponseDTO(Booking booking) {
        UserDto booker = new UserDto(
                booking.getBooker().getId(),
                booking.getBooker().getName(),
                booking.getBooker().getEmail()
        );

        ItemDto item = new ItemDto(
                booking.getItem().getName(),
                booking.getItem().getDescription(),
                booking.getItem().isAvailable(),
                booking.getItem().getRequestId(),
                booking.getItem().getId()
        );

        return new BookingResponseDTO(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus().name(),
                booker,
                item
        );
    }
}
