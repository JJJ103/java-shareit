package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Item createItem(Long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public Item updateItem(Long itemId, Long userId, ItemDto itemDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);

        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        // Обновляем поля только при наличии новых значений
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.isAvailable() != null) {
            item.setAvailable(itemDto.isAvailable());
        }

        return itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> getAllItems(Long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> searchItems(String text) {
        return itemRepository.searchByText(text.toLowerCase());
    }
}
