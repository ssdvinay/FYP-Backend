package com.example.fyp.dto;

public class Complaint {

    private final long dealerId;
    private final String complaint;


    public Complaint(long id, String complaint) {
        this.dealerId = id;
        this.complaint = complaint;
    }

    public String getComplaint() {
        return complaint;
    }

    public long getDealerId() {
        return dealerId;
    }
}
