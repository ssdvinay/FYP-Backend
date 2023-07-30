package com.example.fyp.entity;

import javax.persistence.*;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private DealerAssociationId dealerAssociationId;

    private Long customerId;

    private Long price;

    private String bookingStatus;

    private String bookingDate;

    private String bookingTime;

    private String createdAt;
    
    private String feedback;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dealerId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private Dealer dealer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carTypeId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private CarType carType;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productTypeId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private ProductType productType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DealerAssociationId getDealerAssociationId() {
        return dealerAssociationId;
    }

    public void setDealerAssociationId(DealerAssociationId dealerAssociationId) {
        this.dealerAssociationId = dealerAssociationId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }
}
