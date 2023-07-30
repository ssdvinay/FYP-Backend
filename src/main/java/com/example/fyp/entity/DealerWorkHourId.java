package com.example.fyp.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DealerWorkHourId implements Serializable {

    public long dealerId;

    public String day;
}
