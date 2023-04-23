package com.example.fyp.controller;

import com.example.fyp.CustomerUpdateDto;
import com.example.fyp.Response;
import com.example.fyp.Util;
import com.example.fyp.dto.Showroom;
import com.example.fyp.dto.ShowroomFilters;
import com.example.fyp.entity.Customer;
import com.example.fyp.entity.User;
import com.example.fyp.repository.CustomerRepository;
import com.example.fyp.repository.DealerCarProductRepository;
import com.example.fyp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public CustomerController(DealerCarProductRepository dealerCarProductRepository, CustomerRepository customerRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.dealerCarProductRepository = dealerCarProductRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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
}
