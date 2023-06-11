package com.example.fyp.dto;

public class Feedback {

    private final long bookingId;

    private final String feedback;

    public Feedback(long bookingId, String feedback) {
        this.bookingId = bookingId;
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }

    public long getBookingId() {
        return bookingId;
    }
}
