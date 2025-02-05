package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody ItemDto itemDto) {
        Item createdItem = itemService.createItem(userId, itemDto);
        return new ResponseEntity<>(ItemMapper.toItemDto(createdItem), HttpStatus.CREATED);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody ItemDto itemDto) {
        Item updatedItem = itemService.updateItem(itemId, userId, itemDto);
        return ResponseEntity.ok(ItemMapper.toItemDto(updatedItem));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long itemId) {
        ItemResponseDto itemResponseDto = itemService.getItem(itemId);
        return ResponseEntity.ok(itemResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemResponseDto> items = itemService.getAllItems(userId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(@RequestParam String text) {
        List<Item> items = itemService.searchItems(text);
        return ResponseEntity.ok(ItemMapper.toItemDtoList(items));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long itemId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestBody CommentDto commentDto) {
        CommentDto createdComment = itemService.addComment(itemId, userId, commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}