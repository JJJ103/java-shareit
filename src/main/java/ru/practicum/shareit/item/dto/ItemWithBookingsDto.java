package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemWithBookingsDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private LocalDateTime lastBookingStart;
    private LocalDateTime lastBookingEnd;
    private LocalDateTime nextBookingStart;
    private LocalDateTime nextBookingEnd;

    public ItemWithBookingsDto(Long id, String name, String description, Boolean available, Long requestId,
                               LocalDateTime lastBookingStart, LocalDateTime lastBookingEnd,
                               LocalDateTime nextBookingStart, LocalDateTime nextBookingEnd) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
        this.lastBookingStart = lastBookingStart;
        this.lastBookingEnd = lastBookingEnd;
        this.nextBookingStart = nextBookingStart;
        this.nextBookingEnd = nextBookingEnd;
    }
}
