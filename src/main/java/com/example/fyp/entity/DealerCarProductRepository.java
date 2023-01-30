package com.example.fyp.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DealerCarProductRepository extends JpaRepository<DealerCarProduct, DealerAssociationId> {
}