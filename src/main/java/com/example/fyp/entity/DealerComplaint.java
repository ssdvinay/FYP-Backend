package com.example.fyp.entity;

import javax.persistence.*;

@Entity
public class DealerComplaint {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long dealerComplaintId;

    private long dealerId;
    private String complaint;
    private long customerId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dealerId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private Dealer dealer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private Customer customer;

    public Long getDealerComplaintId() {
        return dealerComplaintId;
    }


    public void setDealerComplaintId(Long dealerComplaintId) {
        this.dealerComplaintId = dealerComplaintId;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public long getDealerId() {
        return dealerId;
    }

    public void setDealerId(long dealerId) {
        this.dealerId = dealerId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
