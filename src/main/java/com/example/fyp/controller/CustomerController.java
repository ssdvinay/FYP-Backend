package com.example.fyp.controller;

import com.example.fyp.CustomerUpdateDto;
import com.example.fyp.Response;
import com.example.fyp.Util;
import com.example.fyp.dto.*;
import com.example.fyp.entity.*;
import com.example.fyp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*")
@RequestMapping("/customer")
@RestController
public class CustomerController {

    private final DealerCarProductRepository dealerCarProductRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DealerRepository dealerRepository;
    private final DealerComplaintRepository dealerComplaintRepository;

    private final BookingRepository bookingRepository;
    private final HttpServletRequest request;

    private final DealerWorkHourRepository dealerWorkHourRepository;

    @Autowired
    public CustomerController(DealerCarProductRepository dealerCarProductRepository,
                              CustomerRepository customerRepository,
                              PasswordEncoder passwordEncoder,
                              UserRepository userRepository,
                              DealerRepository dealerRepository,
                              DealerComplaintRepository dealerComplaintRepository,
                              BookingRepository bookingRepository, HttpServletRequest request, DealerWorkHourRepository dealerWorkHourRepository) {
        this.dealerCarProductRepository = dealerCarProductRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.dealerRepository = dealerRepository;
        this.dealerComplaintRepository = dealerComplaintRepository;
        this.bookingRepository = bookingRepository;
        this.request = request;
        this.dealerWorkHourRepository = dealerWorkHourRepository;
    }

    @PutMapping("/showrooms")
    public List<Showroom> getShowrooms(@RequestBody ShowroomFilters showroomFilters) {
        int numCarTypes = showroomFilters.carTypes.size();
        int numProductTypes = showroomFilters.productTypes.size();
        Set<Long> carTypeFilters = new HashSet<>(showroomFilters.carTypes.isEmpty() ? Arrays.asList(1L, 2L, 3L) : showroomFilters.carTypes);
        Set<Long> productTypeFilters = new HashSet<>(showroomFilters.productTypes.isEmpty() ? Arrays.asList(1L, 2L, 3L) : showroomFilters.productTypes);
        return this.dealerCarProductRepository.getFilteredDealers(showroomFilters.minPrice,
                showroomFilters.maxPrice,
                carTypeFilters,
                productTypeFilters,
                numCarTypes,
                numProductTypes);
    }

    @GetMapping("dealers/{id}/carTypes")
    public List<CarType> getSupportedCarTypesByDealerId(@PathVariable("id") Long id) {
        return dealerCarProductRepository.getSupportedCarTypesOfDealer(id);
    }

    @GetMapping("dealers/{id}/productTypes")
    public List<ProductType> getSupportedProductTypesByDealerId(@PathVariable("id") Long id) {
        return dealerCarProductRepository.getSupportedProductTypesOfDealer(id);
    }

    @PostMapping("/booking")
    public ResponseEntity<Response<String>> saveBooking(@RequestBody Booking booking) {
        booking.setCustomerId(getCustomerId());
        try {
            this.bookingRepository.save(booking);
            return new ResponseEntity<>(new Response<>("Booking successfully created", ""), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Response<>("unable to create booking", ""), HttpStatus.OK);
        }
    }

    @GetMapping("/{dealerId}/bookings")
    public List<String> getDealerBookingTimesOnDate(@PathVariable Long dealerId, @RequestParam("date") String date) {
        return this.bookingRepository.findBookingsByBookingDateAndDealerIdAndBookingStatusIsNot(date, dealerId, "CANCELLED")
                .stream()
                .map(Booking::getBookingTime)
                .toList();
    }

    @GetMapping("/dealers/{dealerId}/hours")
    public List<WorkHour> getDealerWorkHours(@PathVariable Long dealerId) {
        return this.dealerWorkHourRepository.getDealerWorkHourByDealerWorkHourId_DealerId(dealerId)
                .stream()
                .map(dwh -> new WorkHour(dwh.getDealerWorkHourId().day, dwh.getWorkFrom(), dwh.getWorkTo()))
                .toList();
    }

    @PutMapping("/booking/feedback")
    public ResponseEntity<Response<String>> submitFeedback(@RequestBody Feedback feedback) {
        Booking booking = this.bookingRepository.getReferenceById(feedback.getBookingId());
        booking.setFeedback(feedback.getFeedback());
        this.bookingRepository.save(booking);
        return new ResponseEntity<>(new Response<>("Successfully submitted feedback!"), HttpStatus.OK);
    }


    @GetMapping("/bookings")
    public List<Booking> findCustomerBookings() {
        return this.bookingRepository.findBookingsByCustomerIdOrderByCreatedAtDesc(getCustomerId());
    }

    @PutMapping("/complain")
    public ResponseEntity<Response<String>> complain(@RequestBody Complaint complaint) {
        DealerComplaint dealerComplaint = new DealerComplaint();
        dealerComplaint.setDealerId(complaint.getDealerId());
        dealerComplaint.setCustomerId(getCustomerId());
        dealerComplaint.setComplaint(complaint.getComplaint());
        this.dealerComplaintRepository.save(dealerComplaint);
        Dealer dealer = this.dealerRepository.findDealerById(complaint.getDealerId());
        dealer.setComplaints(dealer.getComplaints() + 1);
        if (dealer.getComplaints() >= 3)
            dealer.getUser().setBlacklisted(true);
        this.dealerRepository.save(dealer);
        return new ResponseEntity<>(new Response<>("Complaint lodged successfully!"), HttpStatus.OK);
    }

    @GetMapping("/dealers")
    public List<Dealer> getAllDealers() {
        return this.dealerRepository.findActiveDealers();
    }

    @GetMapping("/image/{dealerId}/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable long dealerId, @PathVariable String filename) {
        try {
            String filePath = "pictures/" + dealerId + "/" + filename;
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{emailOrUsername}")
    public ResponseEntity<Response<CustomerUpdateDto>> getCustomerDetails(@PathVariable("emailOrUsername") String emailOrUsername) {
        try {
            Customer customer = customerRepository.findCustomerByEmailOrUsername(emailOrUsername);
            if (customer == null)
                throw new RuntimeException("Customer not found!");
            CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto();
            customerUpdateDto.setId(customer.getId());
            customerUpdateDto.setFirstName(customer.getUser().getFirstName());
            customerUpdateDto.setLastName(customer.getUser().getLastName());
            customerUpdateDto.setPhoneNumber(customer.getUser().getPhoneNumber());
            customerUpdateDto.setEmail(customer.getUser().getEmail());
            customerUpdateDto.setUsername(customer.getUser().getUsername());
            customerUpdateDto.setAddress(customer.getAddress());
            return new ResponseEntity<>(new Response<>(customerUpdateDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(Util.getRootCause(e)),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response<String>> update(@RequestBody CustomerUpdateDto dto) {
        Customer existingCustomer = customerRepository.findCustomerById(dto.getId());
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        else
            user.setPassword(existingCustomer.getUser().getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(existingCustomer.getUser().getRole());
        user.setBlacklisted(existingCustomer.getUser().getBlacklisted());
        userRepository.save(user);
        Customer customer = new Customer();
        customer.setAddress(dto.getAddress());
        customer.setId(dto.getId());
        customer.setUser(user);
        customerRepository.save(customer);
        return new ResponseEntity<>(new Response<>("Customer Updated Successfully"), HttpStatus.OK);
    }

    private long getCustomerId() {
        return customerRepository.findCustomerByEmailOrUsername(Util.getEmailOrUserNameFromRequest(request)).getId();
    }
}
