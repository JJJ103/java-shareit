package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND (:state = 'ALL' OR b.status = :state) ORDER BY b.start DESC")
    List<Booking> findBookingsByUserIdAndState(Long userId, String state);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :userId AND (:state = 'ALL' OR b.status = :state) ORDER BY b.start DESC")
    List<Booking> findBookingsByOwnerIdAndState(Long userId, String state);
}
