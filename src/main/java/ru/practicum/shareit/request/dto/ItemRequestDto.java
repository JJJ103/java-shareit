package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    private Long id;
    private String description;
    @NotNull
    private Long requestorId; // айди пользователя, который запрашивает вещь
    @NotNull
    @PastOrPresent
    private LocalDateTime created;
}