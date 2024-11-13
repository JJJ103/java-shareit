package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, ItemDto itemDto);

    Item updateItem(Long itemId, Long userId, ItemDto itemDto);

    ItemResponseDto getItem(Long itemId);

    List<ItemResponseDto> getAllItems(Long userId);

    CommentDto addComment(Long itemId, Long userId, CommentDto commentDto);

    List<Item> searchItems(String text);
}
