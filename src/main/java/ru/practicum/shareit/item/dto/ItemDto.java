package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
    private Boolean available; // доступность аренды
    private Long requestId;
    private Long id;  // Добавляем id

    public ItemDto(String name, String description, Boolean available, Long requestId, Long id) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
        this.id = id;  // Инициализация id
    }


    public Boolean isAvailable() {
        return available;
    }
}