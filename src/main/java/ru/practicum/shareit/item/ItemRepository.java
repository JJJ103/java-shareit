package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private long itemIdCounter = 1;

    public Item createItem(Item item) {
        item.setId(itemIdCounter++);
        items.add(item);
        return item;
    }

    public Item getItem(Long itemId) {
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    public List<Item> getAllItemsByOwnerId(Long userId) {
        return items.stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    public Item updateItem(Item item) {
        return item;
    }

    public List<Item> searchItems(String text) {
        return items.stream()
                .filter(item -> (item.getName().contains(text) || item.getDescription().contains(text)) && item.isAvailable())
                .toList();
    }
}
