package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    @NotBlank(message = "Поле имени предмета обязательно")
    private String name;
    @NotBlank(message = "Поле описания предмета обязательно")
    private String description;
    @NotNull(message = "Поле доступности предмета обязательно")
    private Boolean available; //доступность аренды
    private Long requestId;

    public ItemDto(String name, String description, Boolean available, Long requestId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
    }
}