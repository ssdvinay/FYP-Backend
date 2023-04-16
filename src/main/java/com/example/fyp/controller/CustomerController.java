package com.example.fyp.controller;

import com.example.fyp.dto.Showroom;
import com.example.fyp.dto.ShowroomFilters;
import com.example.fyp.repository.DealerCarProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/customer")
@RestController
public class CustomerController {

    private final DealerCarProductRepository dealerCarProductRepository;

    @Autowired
    public CustomerController(DealerCarProductRepository dealerCarProductRepository) {
        this.dealerCarProductRepository = dealerCarProductRepository;
    }

    @PutMapping("/showrooms")
    public List<Showroom> getShowrooms(@RequestBody ShowroomFilters showroomFilters) {
        return this.dealerCarProductRepository.getFilteredDealers(showroomFilters.minPrice,
                showroomFilters.maxPrice,
                new HashSet<>(showroomFilters.carTypes),
                new HashSet<>(showroomFilters.productTypes),
                "APPROVED",
                false);
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
