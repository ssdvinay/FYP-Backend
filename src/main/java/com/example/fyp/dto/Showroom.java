package com.example.fyp.dto;

import com.example.fyp.entity.Dealer;

public class Showroom {

    private final Dealer dealer;
    private final double price;

    private double rating;

    public Showroom(Dealer dealer, double price) {
        this.dealer = dealer;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
