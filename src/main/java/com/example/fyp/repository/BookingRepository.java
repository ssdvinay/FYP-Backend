package com.example.fyp.repository;

import com.example.fyp.dto.MyCustomer;
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

    List<Booking> findBookingsByBookingDateAndDealerIdAndBookingStatusIsNot(String bookingDate, Long dealerId, String bookingStatus);

    @Transactional
    @Modifying
    @Query("update Booking b set b.bookingStatus = :status where b.id = :id")
    void updateBookingStatus(@Param("id") long id, @Param("status") String status);

    @Query("select new com.example.fyp.dto.MyCustomer(" +
            "CONCAT(b.customer.user.firstName, ' ', b.customer.user.lastName), " +
            "b.customer.user.phoneNumber, " +
            "b.customer.user.email, " +
            "count(b)," +
            "(select count(dc) from DealerComplaint dc where dc.dealerId = b.dealerAssociationId.dealerId and dc.customerId = b.customerId)) " +
            "from Booking b " +
            "where b.dealerAssociationId.dealerId = :dealerId " +
            "group by b.customerId")
    List<MyCustomer> getMyCustomers(@Param("dealerId") long dealerId);

    @Query("select coalesce(avg(b.rating), 0) from Booking b where b.dealer.id = :dealerId and b.bookingStatus = 'CONFIRMED' and b.rating > 0 group by b.dealer.id")
    Double getDealerRating(@Param("dealerId") Long dealerId);
}
