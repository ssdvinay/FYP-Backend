package com.example.fyp.controller;

import com.example.fyp.Response;
import com.example.fyp.SignupDto;
import com.example.fyp.Util;
import com.example.fyp.entity.*;
import com.example.fyp.repository.CustomerRepository;
import com.example.fyp.repository.DealerCarProductRepository;
import com.example.fyp.repository.DealerRepository;
import com.example.fyp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DealerRepository dealerRepository;
    private final CustomerRepository customerRepository;
    private final DealerCarProductRepository dealerCarProductRepository;

    @Autowired
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          DealerRepository dealerRepository,
                          CustomerRepository customerRepository,
                          DealerCarProductRepository dealerCarProductRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dealerRepository = dealerRepository;
        this.customerRepository = customerRepository;
        this.dealerCarProductRepository = dealerCarProductRepository;
    }

    @GetMapping("/login")
    public ResponseEntity<Response<String>> login() {
        return new ResponseEntity<>(new Response<>("Login Successful", null), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Response<String>> register(@RequestParam("file") MultipartFile file, @RequestParam("user") String data) throws IOException {
        SignupDto dto = new ObjectMapper().readValue(data, SignupDto.class);

        if (userRepository.existsUserByUsername(dto.getUsername()))
            return new ResponseEntity<>(new Response<>("Username already exists!"), HttpStatus.BAD_REQUEST);

        if (userRepository.existsUserByEmail(dto.getEmail()))
            return new ResponseEntity<>(new Response<>("Email already exists!"), HttpStatus.BAD_REQUEST);

        if (dto.isInvalid()) {
            return new ResponseEntity<>(new Response<>("Invalid request. Missing required information."), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setBlacklisted(false);
        user.setPhoneNumber(dto.getPhoneNumber());

        try {
            userRepository.save(user);
            if (dto.getRole().equals("DEALER")) {
                String path = "pictures/" + user.getId() + "/";
                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdirs();

                try (FileOutputStream fileOutputStream = new FileOutputStream(path + file.getOriginalFilename())) {
                    fileOutputStream.write(file.getBytes());
                }
                Dealer dealer = new Dealer();
                dealer.setId(user.getId());
                dealer.setUser(user);
                dealer.setShowRoomAddress(dto.getAddress());
                dealer.setApprovalStatus("PENDING");
                dealer.setShowroomPicture(file.getOriginalFilename());
                dealerRepository.save(dealer);
                List<DealerCarProduct> dealerCarProductList = new ArrayList<>();
                for (Long carType : dto.getSupportedCarTypes()) {
                    for (Long productType : dto.getSupportedProductTypes()) {
                        DealerAssociationId dealerAssociationId = new DealerAssociationId();
                        dealerAssociationId.dealerId = dealer.getId();
                        dealerAssociationId.carTypeId = carType;
                        dealerAssociationId.productTypeId = productType;
                        DealerCarProduct dealerCarProduct = new DealerCarProduct();
                        dealerCarProduct.setDealerAssociationId(dealerAssociationId);
                        dealerCarProduct.setPrice(dto.getPrice());
                        dealerCarProductList.add(dealerCarProduct);
                    }
                }
                dealerCarProductRepository.saveAll(dealerCarProductList);
            } else if (dto.getRole().equals("CUSTOMER")) {
                Customer customer = new Customer();
                customer.setId(user.getId());
                customer.setUser(user);
                customer.setAddress(dto.getAddress());
                customerRepository.save(customer);
            }

            return new ResponseEntity<>(new Response<>("User registered successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(Util.getRootCause(e)), HttpStatus.BAD_REQUEST);
        }
    }
}
