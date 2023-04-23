package com.example.fyp.controller;

import com.example.fyp.Response;
import com.example.fyp.UpdateDto;
import com.example.fyp.Util;
import com.example.fyp.entity.Dealer;
import com.example.fyp.entity.DealerAssociationId;
import com.example.fyp.entity.DealerCarProduct;
import com.example.fyp.entity.User;
import com.example.fyp.repository.DealerCarProductRepository;
import com.example.fyp.repository.DealerRepository;
import com.example.fyp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/dealer")
@RestController
public class DealerController {

    private final DealerRepository dealerRepository;

    private final DealerCarProductRepository dealerCarProductRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DealerController(DealerRepository dealerRepository, DealerCarProductRepository dealerCarProductRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.dealerRepository = dealerRepository;
        this.dealerCarProductRepository = dealerCarProductRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<Response<String>> update(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("user") String data) throws IOException {
        UpdateDto dto = new ObjectMapper().readValue(data, UpdateDto.class);

        if (dto.isInvalid()) {
            return new ResponseEntity<>(new Response<>("Invalid request. Missing required information."), HttpStatus.BAD_REQUEST);
        }

        Dealer existingDealer = dealerRepository.findDealerById(dto.getId());

        if (file != null) {
            String path = "pictures/" + dto.getId() + "/";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            try (FileOutputStream fileOutputStream = new FileOutputStream(path + file.getOriginalFilename())) {
                fileOutputStream.write(file.getBytes());
            }

            File oldFile = new File(path + existingDealer.getShowroomPicture());
            oldFile.delete();
        }


        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        else
            user.setPassword(existingDealer.getUser().getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(existingDealer.getUser().getRole());
        user.setBlacklisted(existingDealer.getUser().getBlacklisted());
        userRepository.save(user);
        Dealer dealer = new Dealer();
        dealer.setId(user.getId());
        dealer.setUser(user);
        dealer.setShowRoomAddress(dto.getAddress());
        dealer.setApprovalStatus(existingDealer.getApprovalStatus());
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

        dealerCarProductRepository.deleteAllByDealerId(dealer.getId());
        dealerCarProductRepository.saveAll(dealerCarProductList);
        dealer.setDealerCarProductList(new HashSet<>(dealerCarProductList));
        dealer.setShowroomPicture(file == null ? existingDealer.getShowroomPicture() : file.getOriginalFilename());
        dealerRepository.save(dealer);
        return new ResponseEntity<>(new Response<>("Dealer Updated Successfully"), HttpStatus.OK);
    }

    @GetMapping("/{emailOrUsername}")
    public ResponseEntity<Response<UpdateDto>> getDealerDetails(@PathVariable("emailOrUsername") String emailOrUsername) {
        try {
            Dealer dealer = dealerRepository.findByEmailOrUsername(emailOrUsername);
            if (dealer == null)
                throw new RuntimeException("Dealer not found!");
            List<Long> supportedCarTypes = dealerCarProductRepository.findSupportedCarTypesByDealer(dealer.getId());
            List<Long> supportedProductTypes = dealerCarProductRepository.findSupportedProductTypesByDealer(dealer.getId());
            Double price = dealerCarProductRepository.findPriceByDealerId(dealer.getId());
            UpdateDto updateDto = new UpdateDto();
            updateDto.setId(dealer.getId());
            updateDto.setFirstName(dealer.getUser().getFirstName());
            updateDto.setLastName(dealer.getUser().getLastName());
            updateDto.setPassword(dealer.getUser().getPassword());
            updateDto.setEmail(dealer.getUser().getEmail());
            updateDto.setUsername(dealer.getUser().getUsername());
            updateDto.setPhoneNumber(dealer.getUser().getPhoneNumber());
            updateDto.setSupportedCarTypes(supportedCarTypes);
            updateDto.setSupportedProductTypes(supportedProductTypes);
            updateDto.setAddress(dealer.getShowRoomAddress());
            updateDto.setShowroomPicture(dealer.getShowroomPicture());
            updateDto.setPrice(price);
            return new ResponseEntity<>(new Response<>(updateDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response<>(Util.getRootCause(e)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
}