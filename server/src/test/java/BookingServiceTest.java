import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingUnavailableException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Alice", "alice@example.com");
        User booker = new User(2L, "Bob", "bob@example.com"); // Новый пользователь

        item = new Item(1L, "Drill", "Powerful drill", true, user, null);
        bookingDto = new BookingDto(1L, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item.getId(), booker.getId(), BookingStatus.WAITING);
        booking = new Booking(1L, bookingDto.getStart(), bookingDto.getEnd(), item, booker, BookingStatus.WAITING);
    }

    @Test
    void createBooking_ShouldSaveBooking() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User(2L, "Bob", "bob@example.com"))); // Новый пользователь
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(2L, bookingDto); // Бронирует другой пользователь

        assertNotNull(createdBooking);
        assertEquals(BookingStatus.WAITING, createdBooking.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void confirmBooking_ShouldUpdateStatus() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking); // Добавляем мок сохранения

        Booking confirmedBooking = bookingService.confirmBooking(booking.getId(), item.getOwner().getId(), true);

        assertNotNull(confirmedBooking);
        assertEquals(BookingStatus.APPROVED, confirmedBooking.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getBooking_ShouldReturnBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.getBooking(booking.getId(), user.getId());

        assertEquals(booking.getId(), foundBooking.getId());
    }

    @Test
    void getBooking_ShouldThrowException_WhenNotFound() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBooking(999L, user.getId()));
    }

    @Test
    void getAllBookings_ShouldReturnList() {
        when(bookingRepository.findByBookerIdOrderByStartDesc(user.getId()))
                .thenReturn(List.of(booking));

        List<Booking> bookings = bookingService.getAllBookings(user.getId(), BookingState.ALL);

        assertFalse(bookings.isEmpty());
    }

    @Test
    void createBooking_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        BookingDto dto = new BookingDto();
        dto.setItemId(1L);

        assertThrows(BookingNotFoundException.class, () -> bookingService.createBooking(99L, dto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_ShouldThrowForbiddenException_WhenItemOwnerIsSameAsBooker() {
        User owner = new User(1L, "Alice", "a@ex.com");
        Item item = new Item(1L, "Drill", "desc", true, owner, null);

        when(userRepository.findById(owner.getId())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());

        assertThrows(ForbiddenException.class, () -> bookingService.createBooking(owner.getId(), dto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBooking_ShouldThrowBookingUnavailableException_WhenItemNotAvailable() {
        User booker = new User(2L, "Bob", "bob@ex.com");
        Item item = new Item(1L, "Drill", "desc", false, new User(1L, "Alice", "a@ex.com"), null);

        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDto dto = new BookingDto();
        dto.setItemId(item.getId());

        assertThrows(BookingUnavailableException.class, () -> bookingService.createBooking(booker.getId(), dto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void confirmBooking_ShouldRemainApproved_WhenApprovedAlready() {
        Booking existing = new Booking();
        existing.setId(5L);
        existing.setStatus(BookingStatus.WAITING);
        User owner = new User(3L, "Owner", "own@ex.com");
        Item item = new Item();
        item.setId(10L);
        item.setOwner(owner);
        existing.setItem(item);

        when(bookingRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.confirmBooking(existing.getId(), owner.getId(), false);
        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }

    @Test
    void getBookingsForOwner_ShouldThrowUserNotFound_WhenUserNotExists() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> bookingService.getBookingsForOwner(99L, BookingState.ALL));
        verify(bookingRepository, never()).findByItemOwnerIdOrderByStartDesc(anyLong());
    }
}
