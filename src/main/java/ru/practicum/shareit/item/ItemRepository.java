package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

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
        return findItemById(itemId);
    }

    public List<Item> getAllItemsByOwnerId(Long userId) {
        return items.stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    public Item updateItem(Long itemId, Item newItem) {
        Item existingItem = findItemById(itemId);
        existingItem.setName(newItem.getName());
        existingItem.setDescription(newItem.getDescription());
        existingItem.setAvailable(newItem.isAvailable());
        return existingItem;
    }

    public List<Item> searchItems(String text) {
        return items.stream()
                .filter(item -> (item.getName().contains(text) || item.getDescription().contains(text)) && item.isAvailable())
                .toList();
    }

    private Item findItemById(Long id) {
        return items.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}