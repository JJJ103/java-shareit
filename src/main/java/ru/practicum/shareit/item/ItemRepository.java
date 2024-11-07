package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();
    private long itemIdCounter = 1;

    public Item createItem(Item item) {
        item.setId(itemIdCounter++);
        items.put(item.getId(), item);
        return item;
    }

    public Item getItem(Long itemId) {
        return findItemById(itemId);
    }

    public List<Item> getAllItemsByOwnerId(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public Item updateItem(Long itemId, Item newItem) {
        Item existingItem = findItemById(itemId);
        if (existingItem != null) {
            if (newItem.getName() != null) {
                existingItem.setName(newItem.getName());
            }
            if (newItem.getDescription() != null) {
                existingItem.setDescription(newItem.getDescription());
            }
            if (newItem.isAvailable() != null) {
                existingItem.setAvailable(newItem.isAvailable());
            }
        }
        return existingItem;
    }

    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of(); // Возвращаем пустой список
        }

        String searchText = text.toLowerCase();
        return items.values().stream()
                .filter(item -> {
                    String itemName = item.getName() != null ? item.getName().toLowerCase() : "";
                    String itemDescription = item.getDescription() != null ? item.getDescription().toLowerCase() : "";

                    return itemName.contains(searchText) || itemDescription.contains(searchText);
                })
                .filter(Item::isAvailable) //проверяем доступность
                .collect(Collectors.toList());
    }

    private Item findItemById(Long id) {
        return items.get(id);
    }
}
