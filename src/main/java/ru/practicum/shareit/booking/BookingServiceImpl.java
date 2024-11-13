package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingUnavailableException;
import ru.practicum.shareit.exceptions.ForbiddenException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
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
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking confirmBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        // Проверяем, что пользователь либо автор бронирования, либо владелец вещи
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException();
        }

        return booking;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings(Long userId, BookingState state) {
        return switch (state) {
            case CURRENT -> bookingRepository.findCurrentBookings(userId);
            case PAST -> bookingRepository.findPastBookings(userId);
            case FUTURE -> bookingRepository.findFutureBookings(userId);
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            default -> bookingRepository.findByBookerIdOrderByStartDesc(userId);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsForOwner(Long userId, BookingState state) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }

        switch (state) {
            case CURRENT:
                return bookingRepository.findCurrentBookingsForOwner(userId);
            case PAST:
                return bookingRepository.findPastBookingsForOwner(userId);
            case FUTURE:
                return bookingRepository.findFutureBookingsForOwner(userId);
            case WAITING:
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            case ALL:
            default:
                return bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
        }
    }
}
