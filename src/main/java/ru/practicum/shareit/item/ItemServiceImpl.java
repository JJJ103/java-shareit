package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import java.time.LocalDateTime;

import java.util.Collections;
import java.util.Comparator;
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
    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(ItemNotFoundException::new);

        Booking lastBooking = bookingRepository.findTopByItemIdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now());
        Booking nextBooking = bookingRepository.findTopByItemIdAndEndAfterOrderByEndDesc(item.getId(), LocalDateTime.now());

        List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        return new ItemResponseDto(
                item,
                lastBooking != null ? BookingMapper.toBookingDto(lastBooking) : null,
                nextBooking != null ? BookingMapper.toBookingDto(nextBooking) : null,
                comments
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<ItemResponseDto> getAllItems(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        return items.stream().map(item -> {
            Booking lastBooking = bookingRepository.findTopByItemIdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now());
            Booking nextBooking = bookingRepository.findTopByItemIdAndEndAfterOrderByEndDesc(item.getId(), LocalDateTime.now());

            List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                    .stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList());

            return new ItemResponseDto(
                    item,
                    lastBooking != null ? BookingMapper.toBookingDto(lastBooking) : null,
                    nextBooking != null ? BookingMapper.toBookingDto(nextBooking) : null,
                    comments
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

        boolean hasApprovedBooking = bookingRepository
                .existsByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (!hasApprovedBooking) {
            throw new BookingNotApprovedException();
        }

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
