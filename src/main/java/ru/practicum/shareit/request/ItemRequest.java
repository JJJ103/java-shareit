package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
public class ItemRequest {
    @NotNull
    private Long id;
    private String description;
    @NotNull
    private User requestor; // Пользователь, который запрашивает вещь
    @NotNull
    @PastOrPresent
    private LocalDateTime created; // Дата и время создания запроса
}
