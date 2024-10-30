package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

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
        if (text == null || text.isBlank()) {
            return List.of(); // Возвращаем пустой список
        }

        return items.stream()
                .filter(item -> {
                    String itemName = item.getName() != null ? item.getName().toLowerCase() : "";
                    String itemDescription = item.getDescription() != null ? item.getDescription().toLowerCase() : "";
                    String searchText = text.toLowerCase();

                    return itemName.contains(searchText) || itemDescription.contains(searchText);
                })
                .filter(Item::isAvailable) // Проверяем доступность
                .toList();
    }

    private Item findItemById(Long id) {
        return items.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
