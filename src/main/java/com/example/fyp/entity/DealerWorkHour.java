package com.example.fyp.entity;

import javax.persistence.*;

@Entity
public class DealerWorkHour {

    @EmbeddedId
    private DealerWorkHourId dealerWorkHourId;

    private String workFrom;

    private String workTo;


    public String getWorkFrom() {
        return workFrom;
    }

    public void setWorkFrom(String workFrom) {
        this.workFrom = workFrom;
    }

    public String getWorkTo() {
        return workTo;
    }

    public void setWorkTo(String workTo) {
        this.workTo = workTo;
    }


    public DealerWorkHourId getDealerWorkHourId() {
        return dealerWorkHourId;
    }

    public void setDealerWorkHourId(DealerWorkHourId dealerWorkHourId) {
        this.dealerWorkHourId = dealerWorkHourId;
    }
}
