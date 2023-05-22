package com.example.fyp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CarType {

    @Id
    private Long id;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
