package com.example.fyp.dto;

import com.example.fyp.entity.Dealer;

public class Showroom {

    private final Dealer dealer;
    private final Double price;

    private Double rating;

    public Showroom(Dealer dealer, Double price) {
        this.dealer = dealer;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
