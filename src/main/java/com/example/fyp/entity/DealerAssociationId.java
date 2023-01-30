package com.example.fyp.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DealerAssociationId implements Serializable {

    public Long dealerId;

    public Long carTypeId;

    public Long productTypeId;
}
