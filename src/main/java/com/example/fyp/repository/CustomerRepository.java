package com.example.fyp.repository;

import com.example.fyp.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.user.email = :emailOrUsername or c.user.username = :emailOrUsername")
    Customer findCustomerByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);

    Customer findCustomerById(Long id);
}