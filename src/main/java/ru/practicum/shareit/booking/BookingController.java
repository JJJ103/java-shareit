package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                                            @RequestHeader("X-Sharer-User-Id") Long userId) {

        Booking booking = bookingService.createBooking(userId, bookingDto);

        BookingResponseDTO response = new BookingResponseDTO(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> confirmBooking(@PathVariable Long bookingId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam Boolean approved) {
        Booking booking = bookingService.confirmBooking(bookingId, userId, approved);

        BookingResponseDTO response = new BookingResponseDTO(booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDTO> getBooking(@PathVariable Long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking booking = bookingService.getBooking(bookingId, userId);
        BookingResponseDTO response = new BookingResponseDTO(booking);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {
        List<Booking> bookings = bookingService.getAllBookings(userId, state);
        List<BookingResponseDTO> bookingResponseDtos = bookings.stream()
                .map(BookingResponseDTO::new)
                .toList();
        return ResponseEntity.ok(bookingResponseDtos);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsForOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state) {

        List<Booking> bookings = bookingService.getBookingsForOwner(userId, state);

        List<BookingResponseDTO> bookingResponseDtos = bookings.stream()
                .map(BookingResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookingResponseDtos);
    }
}
