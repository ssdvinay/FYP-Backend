package com.example.fyp.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DealerCarProduct {

    @EmbeddedId
    private DealerAssociationId dealerAssociationId;

    private Double price;
    @ManyToOne
    @JoinColumn(name = "dealerId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private Dealer dealer;
    @JoinColumn(name = "carTypeId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    @ManyToOne
    private CarType carType;

    @JoinColumn(name = "productTypeId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    @ManyToOne
    private ProductType productType;


    public DealerAssociationId getDealerAssociationId() {
        return dealerAssociationId;
    }

    public void setDealerAssociationId(DealerAssociationId dealerAssociationId) {
        this.dealerAssociationId = dealerAssociationId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }
}
