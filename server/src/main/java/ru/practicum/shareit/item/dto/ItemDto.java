package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    @NotBlank(message = "Поле имени предмета обязательно")
    private String name;
    @NotBlank(message = "Поле описания предмета обязательно")
    private String description;
    @NotNull(message = "Поле доступности предмета обязательно")
    private Boolean available;
    private Long requestId;
    private Long id;
    private List<CommentDto> comments;

    public ItemDto(String name, String description, Boolean available, Long requestId, Long id) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
        this.id = id;
    }

    public Boolean isAvailable() {
        return available;
    }
}
