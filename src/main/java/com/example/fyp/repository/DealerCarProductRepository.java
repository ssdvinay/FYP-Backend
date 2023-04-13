package com.example.fyp.repository;

import com.example.fyp.dto.Showroom;
import com.example.fyp.entity.DealerAssociationId;
import com.example.fyp.entity.DealerCarProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DealerCarProductRepository extends JpaRepository<DealerCarProduct, DealerAssociationId> {

    @Query("SELECT new com.example.fyp.dto.Showroom(d.dealer, d.price) FROM DealerCarProduct d WHERE d.price >= :minPrice and d.price <= :maxPrice and d.dealerAssociationId.carTypeId IN :carTypes and d.dealerAssociationId.productTypeId IN :productTypes group by d.dealerAssociationId.dealerId, d.price")
    List<Showroom> getFilteredDealers(@Param("minPrice") double minPrice,
                                      @Param("maxPrice") double maxPrice,
                                      @Param("carTypes") Set<Long> carTypes,
                                      @Param("productTypes") Set<Long> productTypes);
}