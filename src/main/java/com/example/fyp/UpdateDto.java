package com.example.fyp;

import java.util.List;

public class UpdateDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String username;
    private String email;
    private String password;
    private String address;
    private List<Long> supportedCarTypes;
    private List<Long> supportedProductTypes;
    private double price;
    private String showroomPicture;
    private double latitude;
    private double longitude;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isInvalid() {
        return getUsername().isBlank()
                || getEmail().isBlank()
                || getFirstName().isBlank()
                || getLastName().isBlank()
                || getSupportedProductTypes().isEmpty()
                || getSupportedCarTypes().isEmpty();
    }

    public List<Long> getSupportedCarTypes() {
        return supportedCarTypes;
    }

    public void setSupportedCarTypes(List<Long> supportedCarTypes) {
        this.supportedCarTypes = supportedCarTypes;
    }

    public List<Long> getSupportedProductTypes() {
        return supportedProductTypes;
    }

    public void setSupportedProductTypes(List<Long> supportedProductTypes) {
        this.supportedProductTypes = supportedProductTypes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShowroomPicture() {
        return showroomPicture;
    }

    public void setShowroomPicture(String showroomPicture) {
        this.showroomPicture = showroomPicture;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
