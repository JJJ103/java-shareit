package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestResponseDto {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private List<ItemSummaryDto> items;

    public ItemRequestResponseDto(ItemRequestDto requestDto, List<ItemSummaryDto> itemDtos) {
        this.id = requestDto.getId();
        this.description = requestDto.getDescription();
        this.requestorId = requestDto.getRequestorId();
        this.created = requestDto.getCreated();
        this.items = itemDtos;
    }
}
