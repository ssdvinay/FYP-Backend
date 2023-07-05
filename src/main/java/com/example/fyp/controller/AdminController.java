package com.example.fyp.controller;

import com.example.fyp.Response;
import com.example.fyp.Util;
import com.example.fyp.dto.CustomerComplaint;
import com.example.fyp.dto.DealerComplaintsCount;
import com.example.fyp.entity.Booking;
import com.example.fyp.entity.Customer;
import com.example.fyp.entity.Dealer;
import com.example.fyp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final DealerRepository dealerRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final DealerComplaintRepository dealerComplaintRepository;

    @Autowired
    public AdminController(UserRepository userRepository,
                           DealerRepository dealerRepository,
                           CustomerRepository customerRepository,
                           BookingRepository bookingRepository, DealerComplaintRepository dealerComplaintRepository) {
        this.userRepository = userRepository;
        this.dealerRepository = dealerRepository;
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.dealerComplaintRepository = dealerComplaintRepository;
    }

    @PutMapping("/blacklist/{id}")
    public ResponseEntity<Response<String>> setBlacklistUser(@PathVariable("id") Long id, @RequestParam("isBlacklisted") Boolean isBlacklisted) {
        if (id == null || isBlacklisted == null)
            return new ResponseEntity<>(new Response<>("Null id or isBlacklisted value"), HttpStatus.BAD_REQUEST);
        try {
            this.userRepository.setBlacklistUser(id, isBlacklisted);
            return new ResponseEntity<>(new Response<>("Successfully updated user", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>("Could not updated user because:\n" + Util.getRootCause(e)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/complaints")
    public List<CustomerComplaint> getAllComplains() {
        return this.dealerComplaintRepository.findAllCustomerComplaints();
    }

    @GetMapping("/complaints/count")
    public List<DealerComplaintsCount> getComplainsCount() {
        return this.dealerRepository.findAllNumberOfDealerComplaints();
    }

    @GetMapping("/dealers")
    public List<Dealer> getAllDealers() {
        return this.dealerRepository.findAll();
    }

    @GetMapping("/dealers/{id}")
    public ResponseEntity<Response<Dealer>> getDealerDetails(@PathVariable("id") Long id) {
        try {
            Dealer dealer = dealerRepository.findDealerById(id);
            if (dealer == null)
                throw new RuntimeException("Dealer not found!");
            return new ResponseEntity<>(new Response<>(dealer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(Util.getRootCause(e)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Response<Customer>> getCustomerDetails(@PathVariable("id") Long id) {
        try {
            Customer customer = customerRepository.findCustomerById(id);
            if (customer == null)
                throw new RuntimeException("Customer not found!");
            return new ResponseEntity<>(new Response<>(customer), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(Util.getRootCause(e)),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bookings")
    public List<Booking> getAllBookings() {
        return this.bookingRepository.findAll();
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<Response<List<Dealer>>> getPendingDealerRequests() {
        try {
            List<Dealer> pendingDealersList = dealerRepository.findDealersByApprovalStatus("PENDING");
            return new ResponseEntity<>(new Response<>(pendingDealersList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(Util.getRootCause(e)),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<Response<String>> updateDealerRegistrationStatus(@PathVariable("id") Long dealerId,
                                                                           @RequestParam("status") String status) {
        try {
            dealerRepository.updateDealerRegistrationStatus(dealerId, status);
            return new ResponseEntity<>(new Response<>("Status updated", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(Util.getRootCause(e)),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
