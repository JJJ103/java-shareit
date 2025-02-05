import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.ItemRequestNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    private User user;
    private ItemRequest request;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Alice", "alice@example.com");
        requestDto = new ItemRequestDto(1L, "Need a laptop", user.getId(), LocalDateTime.now());
        request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Need a laptop");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());
    }

    @Test
    void createRequest_ShouldSaveRequest() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(request);

        ItemRequest createdRequest = requestService.createRequest(user.getId(), requestDto);

        assertNotNull(createdRequest);
        assertEquals(request.getDescription(), createdRequest.getDescription());
        verify(requestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void createRequest_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> requestService.createRequest(99L, requestDto));
        verify(requestRepository, never()).save(any(ItemRequest.class));
    }

    @Test
    void getUserRequests_ShouldReturnList() {
        when(requestRepository.findByRequestorId(user.getId())).thenReturn(List.of(request));

        List<ItemRequest> requests = requestService.getUserRequests(user.getId());

        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
        verify(requestRepository, times(1)).findByRequestorId(user.getId());
    }

    @Test
    void getAllRequests_ShouldReturnListExcludingUserRequests() {
        User anotherUser = new User(2L, "Bob", "bob@example.com");
        ItemRequest anotherRequest = new ItemRequest();
        anotherRequest.setId(2L);
        anotherRequest.setDescription("Need a phone");
        anotherRequest.setRequestor(anotherUser);
        anotherRequest.setCreated(LocalDateTime.now());

        when(requestRepository.findAll()).thenReturn(List.of(request, anotherRequest));

        List<ItemRequest> requests = requestService.getAllRequests(user.getId());

        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size()); // Должен остаться только запрос другого пользователя
        assertEquals("Need a phone", requests.get(0).getDescription());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    void getRequest_ShouldReturnRequestResponseDto() {
        Item item = new Item(1L, "Laptop", "MacBook", true, user, request);
        List<Item> items = List.of(item);
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(itemRepository.findByRequest_Id(request.getId())).thenReturn(items);

        ItemRequestResponseDto response = requestService.getRequest(request.getId(), user.getId());

        assertNotNull(response);
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(1, response.getItems().size());
        verify(requestRepository, times(1)).findById(request.getId());
        verify(itemRepository, times(1)).findByRequest_Id(request.getId());
    }

    @Test
    void getRequest_ShouldThrowItemRequestNotFoundException() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class, () -> requestService.getRequest(99L, user.getId()));
        verify(itemRepository, never()).findByRequest_Id(anyLong());
    }
}
