package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingUnavailableException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Booking createBooking(Long userId, BookingDto bookingDto) {
        User booker = userRepository.findById(userId)
                .orElseThrow(BookingNotFoundException::new);

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(BookingNotFoundException::new);

        if (item.getOwner().getId().equals(booker.getId())) {
            throw new ForbiddenException();
        }

        if (!item.getAvailable()) {
            throw new BookingUnavailableException();
        }

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Booking.Status.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking confirmBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        // Проверяем, что запрос подтверждает владелец вещи
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Only item owner can confirm the booking");
        }

        if (approved) {
            booking.setStatus(Booking.Status.APPROVED);
        } else {
            booking.setStatus(Booking.Status.REJECTED);
        }

        bookingRepository.save(booking);
        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        // Проверяем, что пользователь либо автор бронирования, либо владелец вещи
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("User not authorized to view this booking");
        }

        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings(Long userId, String state) {
        return bookingRepository.findBookingsByUserIdAndState(userId, state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsForOwner(Long userId, String state) {
        return bookingRepository.findBookingsByOwnerIdAndState(userId, state);
    }
}
