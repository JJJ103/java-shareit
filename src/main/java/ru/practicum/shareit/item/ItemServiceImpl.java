package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item createItem(Long userId, ItemDto itemDto) {
        User owner = userRepository.getUser(userId);
        if (owner == null) {
            throw new UserNotFoundException();
        }
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        return itemRepository.createItem(item);
    }

    @Override
    public Item updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = itemRepository.getItem(itemId);
        if (item == null || !item.getOwner().getId().equals(userId)) {
            throw new IllegalArgumentException("Item not found or you are not the owner");
        }
        // Обновляем поля
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        item.setAvailable(itemDto.getAvailable());
        return itemRepository.updateItem(item);
    }

    @Override
    public Item getItem(Long itemId) {
        return itemRepository.getItem(itemId);
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return itemRepository.getAllItemsByOwnerId(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemRepository.searchItems(text);
    }
}
