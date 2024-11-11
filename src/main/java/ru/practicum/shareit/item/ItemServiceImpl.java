package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Item createItem(Long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(ItemRequestNotFoundException::new);
        }

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        item.setRequest(request);

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
    public ItemDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);

        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setComments(comments);
        return itemDto;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ItemWithBookingsDto> getAllItems(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        return items.stream().map(item -> {
            LocalDateTime lastBookingStart = null;
            LocalDateTime lastBookingEnd = null;
            LocalDateTime nextBookingStart = null;
            LocalDateTime nextBookingEnd = null;

            // Получаем последнее и следующее бронирование
            List<Booking> bookings = bookingRepository.findByItemIdAndStatus(item.getId(), BookingStatus.APPROVED);

            if (!bookings.isEmpty()) {
                bookings.sort((b1, b2) -> b2.getStart().compareTo(b1.getStart()));

                // Последнее бронирование
                Booking lastBooking = bookings.get(0);
                lastBookingStart = lastBooking.getStart();
                lastBookingEnd = lastBooking.getEnd();

                // Следующее бронирование
                for (Booking booking : bookings) {
                    if (booking.getStart().isAfter(LocalDateTime.now())) {
                        nextBookingStart = booking.getStart();
                        nextBookingEnd = booking.getEnd();
                        break;
                    }
                }
            }

            return new ItemWithBookingsDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.isAvailable(),
                    item.getRequestId(),
                    lastBookingStart,
                    lastBookingEnd,
                    nextBookingStart,
                    nextBookingEnd
            );
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);
        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return new CommentDto(
                savedComment.getId(),
                savedComment.getText(),
                author.getName(),
                savedComment.getCreated()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchByText(text.toLowerCase());
    }
}
