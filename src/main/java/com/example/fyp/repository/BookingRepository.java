package com.example.fyp.repository;

import com.example.fyp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByCustomerIdOrderByCreatedAtDesc(long customerId);

    List<Booking> findBookingsByDealerIdOrderByCreatedAtDesc(long dealerId);

    @Transactional
    @Modifying
    @Query("update Booking b set b.bookingStatus = :status where b.id = :id")
    void updateBookingStatus(@Param("id") long id, @Param("status") String status);
}
