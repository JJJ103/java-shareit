package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor; // Пользователь, который запрашивает вещь
    private LocalDateTime created; // Дата и время создания запроса
}
