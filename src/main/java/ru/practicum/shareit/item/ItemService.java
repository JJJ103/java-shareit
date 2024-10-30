package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, ItemDto itemDto);
    Item updateItem(Long itemId, Long userId, ItemDto itemDto);
    Item getItem(Long itemId);
    List<Item> getAllItems(Long userId);
    List<Item> searchItems(String text);
}
