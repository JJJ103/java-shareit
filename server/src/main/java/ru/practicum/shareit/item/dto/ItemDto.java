package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDto {
    private String name;
    private String description;
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
