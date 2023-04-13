package com.example.fyp.dto;

import java.util.List;

public class ShowroomFilters {
    public final double minPrice;
    public final double maxPrice;
    public final List<Long> carTypes;
    public final List<Long> productTypes;

    public ShowroomFilters(double minPrice, double maxPrice, List<Long> carTypes, List<Long> productTypes) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.carTypes = carTypes;
        this.productTypes = productTypes;
    }
}
