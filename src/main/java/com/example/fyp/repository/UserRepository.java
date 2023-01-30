package com.example.fyp.repository;

import com.example.fyp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameOrEmail(String username, String email);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("update User c set c.isBlacklisted = :isBlacklisted WHERE c.id = :id")
    void setBlacklistUser(@Param("id") Long id, @Param("isBlacklisted") boolean isBlacklisted);
}
