package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";
    private final String serverUrl;

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(restTemplate);
        this.serverUrl = serverUrl;
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingDto bookingDto) {
        String path = serverUrl + API_PREFIX;
        return post(path, userId, bookingDto);
    }

    public ResponseEntity<Object> confirmBooking(Long bookingId, Long userId, Boolean approved) {
        String path = serverUrl + API_PREFIX + "/" + bookingId + "?approved=" + approved;
        return patch(path, userId);
    }

    public ResponseEntity<Object> getBooking(Long bookingId, Long userId) {
        String path = serverUrl + API_PREFIX + "/" + bookingId;
        return get(path, userId);
    }

    public ResponseEntity<Object> getAllBookings(Long userId, String state, Integer from, Integer size) {
        String path = serverUrl + API_PREFIX;
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get(path, userId, parameters);
    }

    public ResponseEntity<Object> getBookingsForOwner(Long userId, String state, Integer from, Integer size) {
        String path = serverUrl + API_PREFIX + "/owner";
        Map<String, Object> parameters = Map.of("state", state, "from", from, "size", size);
        return get(path, userId, parameters);
    }
}
