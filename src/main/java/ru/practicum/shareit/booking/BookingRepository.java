package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByItemIdAndStatus(Long itemId, BookingStatus status);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> findCurrentBookings(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.end < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> findPastBookings(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.start > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> findFutureBookings(@Param("bookerId") Long bookerId);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start <= CURRENT_TIMESTAMP AND b.end >= CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsForOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.end < CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> findPastBookingsForOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.owner.id = :ownerId AND b.start > CURRENT_TIMESTAMP ORDER BY b.start DESC")
    List<Booking> findFutureBookingsForOwner(@Param("ownerId") Long ownerId);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);
}
