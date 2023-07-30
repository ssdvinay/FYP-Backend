package com.example.fyp.repository;

import com.example.fyp.entity.DealerWorkHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealerWorkHourRepository extends JpaRepository<DealerWorkHour, Long> {

    List<DealerWorkHour> getDealerWorkHourByDealerWorkHourId_DealerId(Long dealerId);
}