import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

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
}
