package com.example.fyp.dto;

public class Feedback {

    private final long bookingId;
    private final String feedback;
    private final int rating;

    public Feedback(long bookingId, String feedback, int rating) {
        this.bookingId = bookingId;
        this.feedback = feedback;
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public long getBookingId() {
        return bookingId;
    }

    public int getRating() {
        return rating;
    }
}
