package com.example.fyp.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Dealer")
public class Dealer {

    @Id
    private Long id;

    private String showRoomAddress;

    private String approvalStatus;

    private String showroomPicture;

    private int complaints;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "dealer")
    private Set<DealerCarProduct> dealerCarProductList;

    private double latitude;

    private double longitude;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getShowRoomAddress() {
        return showRoomAddress;
    }

    public void setShowRoomAddress(String showRoomAddress) {
        this.showRoomAddress = showRoomAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Set<DealerCarProduct> getDealerCarProductList() {
        return dealerCarProductList;
    }

    public void setDealerCarProductList(Set<DealerCarProduct> dealerCarProductList) {
        this.dealerCarProductList = dealerCarProductList;
    }

    public String getShowroomPicture() {
        return showroomPicture;
    }

    public void setShowroomPicture(String showroomPicture) {
        this.showroomPicture = showroomPicture;
    }

    public int getComplaints() {
        return complaints;
    }

    public void setComplaints(int complaints) {
        this.complaints = complaints;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
