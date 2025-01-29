import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BookingNotApprovedException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Bob", "bob@example.com");
        item = new Item(1L, "Drill", "Powerful drill", true, user, null);
        itemDto = new ItemDto("Drill", "Powerful drill", true, null, 1L);
    }

    @Test
    void createItem_ShouldSaveItem() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item createdItem = itemService.createItem(user.getId(), itemDto);

        assertNotNull(createdItem);
        assertEquals("Drill", createdItem.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItem_ShouldThrowException_WhenNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItem(999L));
    }

    @Test
    void searchItems_ShouldReturnList() {
        when(itemRepository.searchByText(anyString())).thenReturn(List.of(item));

        List<Item> items = itemService.searchItems("Drill");

        assertFalse(items.isEmpty());
    }

    @Test
    void updateItem_ShouldThrowException_WhenItemNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        ItemDto updateDto = new ItemDto("NewName", "NewDesc", false, null, null);

        assertThrows(ItemNotFoundException.class, () -> itemService.updateItem(99L, user.getId(), updateDto));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldThrowForbiddenException_WhenNotOwner() {
        User anotherOwner = new User(2L, "Other", "other@example.com");
        item.setOwner(anotherOwner);

        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemDto updateDto = new ItemDto("Changed", "Desc", true, null, null);
        assertThrows(ForbiddenException.class, () -> itemService.updateItem(item.getId(), user.getId(), updateDto));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldUpdateFields() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemDto updateDto = new ItemDto("Renamed", "NewDesc", false, null, null);
        Item updated = itemService.updateItem(item.getId(), user.getId(), updateDto);

        assertEquals("Renamed", updated.getName());
        assertEquals("NewDesc", updated.getDescription());
        assertFalse(updated.getAvailable());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addComment_ShouldThrowBookingNotApprovedException_IfNoApprovedBooking() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(item.getId(), user.getId(), BookingStatus.APPROVED, LocalDateTime.now()))
                .thenReturn(false);

        CommentDto commentDto = new CommentDto(null, "Nice item", user.getName(), null);
        assertThrows(BookingNotApprovedException.class, () -> itemService.addComment(item.getId(), user.getId(), commentDto));
        verify(commentRepository, never()).save(any());
    }

    @Test
    void addComment_ShouldSaveComment_WhenBookingApproved() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(anyLong(), anyLong(), eq(BookingStatus.APPROVED), any(LocalDateTime.class)))
                .thenReturn(true);

        Comment comment = new Comment();
        comment.setId(10L);
        comment.setText("Nice item");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setItem(item);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto inputDto = new CommentDto(null, "Nice item", null, null);

        CommentDto result = itemService.addComment(item.getId(), user.getId(), inputDto);
        assertNotNull(result);
        assertEquals("Nice item", result.getText());
        assertEquals("Bob", result.getAuthorName());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
