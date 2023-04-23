package com.example.fyp.repository;

import com.example.fyp.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {

    @Query("select d from Dealer d where d.user.email = :emailOrUsername or d.user.username = :emailOrUsername")
    Dealer findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);

    Dealer findDealerById(Long id);

    List<Dealer> findDealersByApprovalStatus(String approvalStatus);

    @Transactional
    @Modifying
    @Query("update Dealer d set d.approvalStatus=:status where d.id=:id")
    void updateDealerRegistrationStatus(@Param("id") Long id, @Param("status") String status);
}