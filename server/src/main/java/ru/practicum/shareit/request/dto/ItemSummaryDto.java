package ru.practicum.shareit.request.dto;

import lombok.Data;

@Data
public class ItemSummaryDto {
    private Long id;
    private String name;
    private Long ownerId;

    public ItemSummaryDto(Long id, String name, Long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }
}