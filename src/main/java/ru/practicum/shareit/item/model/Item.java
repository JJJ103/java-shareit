package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available; // Доступность для аренды
    private User owner;
    private ItemRequest request; // Запрос, по которому создана вещь

    public Boolean isAvailable() {
        return available;
    }
}
