package com.example.fyp.dto;

public class MyCustomer {
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final long totalBookings;

    private final long totalComplaints;

    public MyCustomer(String name, String phoneNumber, String email, long totalBookings, long totalComplaints) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.totalBookings = totalBookings;
        this.totalComplaints = totalComplaints;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public long getTotalComplaints() {
        return totalComplaints;
    }
}
