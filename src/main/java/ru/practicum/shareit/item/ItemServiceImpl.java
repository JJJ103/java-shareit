package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
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
        Item newItem = ItemMapper.toItem(itemDto);
        if (item == null) {
            throw new ItemNotFoundException();
        }
        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        return itemRepository.updateItem(itemId, newItem);
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
