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
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

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
}
