package com.example.fyp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CarType {

    @Id
    private Long id;

    private String type;
}
