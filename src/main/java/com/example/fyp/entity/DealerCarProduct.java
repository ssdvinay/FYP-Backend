package com.example.fyp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class DealerCarProduct {

    @EmbeddedId
    private DealerAssociationId dealerAssociationId;

    private Double price;

    public DealerAssociationId getDealerAssociationId() {
        return dealerAssociationId;
    }

    @ManyToOne
    @JoinColumn(name = "dealerId", referencedColumnName = "id", nullable = false, updatable = false, insertable = false)
    private Dealer dealer;

    public void setDealerAssociationId(DealerAssociationId dealerAssociationId) {
        this.dealerAssociationId = dealerAssociationId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
