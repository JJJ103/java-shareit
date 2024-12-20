package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String status;
    private UserDto booker;
    private ItemDto item;
}

