package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Boolean available; // Доступность для аренды
    @NotNull
    private User owner;
    private ItemRequest request; // Запрос, по которому создана вещь

    public Boolean isAvailable() {
        return available;
    }
}
