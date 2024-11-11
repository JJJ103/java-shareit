package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingResponseDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private UserDto booker;
    private ItemDto item;

    public BookingResponseDTO(Booking booking) {
        this.id = booking.getId();
        this.start = booking.getStart();
        this.end = booking.getEnd();
        this.status = booking.getStatus().name();

        this.booker = new UserDto(
                booking.getBooker().getId(),
                booking.getBooker().getName(),
                booking.getBooker().getEmail()
        );

        this.item = new ItemDto(
                booking.getItem().getName(),
                booking.getItem().getDescription(),
                booking.getItem().isAvailable(),
                booking.getItem().getRequestId(),
                booking.getItem().getId()
        );
    }
}

