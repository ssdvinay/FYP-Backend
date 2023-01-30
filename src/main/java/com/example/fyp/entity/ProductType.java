package com.example.fyp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductType {

    @Id
    private Long id;

    private String type;
}
