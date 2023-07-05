package com.example.fyp.repository;

import com.example.fyp.dto.CustomerComplaint;
import com.example.fyp.entity.DealerComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DealerComplaintRepository extends JpaRepository<DealerComplaint, Long> {

    @Query("select new com.example.fyp.dto.CustomerComplaint(CONCAT(dc.customer.user.firstName, ' ', dc.customer.user.lastName), CONCAT(dc.dealer.user.firstName, ' ', dc.dealer.user.lastName), dc.complaint) from DealerComplaint dc")
    List<CustomerComplaint> findAllCustomerComplaints();

    @Query("select new com.example.fyp.dto.CustomerComplaint(CONCAT(dc.customer.user.firstName, ' ', dc.customer.user.lastName), CONCAT(dc.dealer.user.firstName, ' ', dc.dealer.user.lastName), dc.complaint) from DealerComplaint dc where dc.dealerId = :id")
    List<CustomerComplaint> findAllCustomerComplaintsByDealerId(@Param("id") long dealerId);
}
