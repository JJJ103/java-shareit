package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;

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
    public ResponseEntity<BookingDto> confirmBooking(@PathVariable Long bookingId,
                                                     @RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam Boolean approved) {
        Booking confirmedBooking = bookingService.confirmBooking(bookingId, userId, approved);
        return ResponseEntity.ok(BookingMapper.toBookingDto(confirmedBooking));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable Long bookingId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        Booking booking = bookingService.getBooking(bookingId, userId);
        return ResponseEntity.ok(BookingMapper.toBookingDto(booking));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.getAllBookings(userId, state);
        List<BookingDto> bookingDtos = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingDtos);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                @RequestParam(defaultValue = "ALL") String state) {
        List<Booking> bookings = bookingService.getBookingsForOwner(userId, state);
        List<BookingDto> bookingDtos = bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingDtos);
    }
}
