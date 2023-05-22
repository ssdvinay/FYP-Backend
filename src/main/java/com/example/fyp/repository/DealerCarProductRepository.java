package com.example.fyp.repository;

import com.example.fyp.dto.Showroom;
import com.example.fyp.entity.CarType;
import com.example.fyp.entity.DealerAssociationId;
import com.example.fyp.entity.DealerCarProduct;
import com.example.fyp.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface DealerCarProductRepository extends JpaRepository<DealerCarProduct, DealerAssociationId> {

    @Query("SELECT new com.example.fyp.dto.Showroom(d.dealer, d.price) FROM DealerCarProduct d " +
            "WHERE d.price >= :minPrice " +
            "and d.price <= :maxPrice " +
            "and d.dealerAssociationId.carTypeId IN :carTypes " +
            "and d.dealerAssociationId.productTypeId IN :productTypes " +
            "and d.dealer.approvalStatus = 'APPROVED' " +
            "and d.dealer.user.isBlacklisted = false " +
            "group by d.dealerAssociationId.dealerId, d.price " +
            "HAVING COUNT(DISTINCT d.dealerAssociationId.carTypeId) >= :numCarTypes " +
            "AND COUNT(DISTINCT d.dealerAssociationId.productTypeId) >= :numProductTypes")
    List<Showroom> getFilteredDealers(@Param("minPrice") double minPrice,
                                      @Param("maxPrice") double maxPrice,
                                      @Param("carTypes") Set<Long> carTypes,
                                      @Param("productTypes") Set<Long> productTypes,
                                      @Param("numCarTypes") long numCarTypes,
                                      @Param("numProductTypes") long numProductTypes);

    @Query("select d.dealerAssociationId.carTypeId from DealerCarProduct d where d.dealerAssociationId.dealerId = :dealerId group by d.dealerAssociationId.carTypeId")
    List<Long> findSupportedCarTypesByDealer(@Param("dealerId") Long dealerId);

    @Query("select d.dealerAssociationId.productTypeId from DealerCarProduct d where d.dealerAssociationId.dealerId = :dealerId group by d.dealerAssociationId.productTypeId")
    List<Long> findSupportedProductTypesByDealer(@Param("dealerId") Long dealerId);

    @Query("select  d.price from DealerCarProduct d where d.dealerAssociationId.dealerId = :dealerId group by d.price")
    Double findPriceByDealerId(@Param("dealerId") Long dealerId);

    @Transactional
    @Modifying
    @Query("delete from DealerCarProduct d where d.dealerAssociationId.dealerId = :dealerId")
    void deleteAllByDealerId(@Param("dealerId") Long dealerId);

    @Query("select d.carType from DealerCarProduct d " +
            "where d.dealerAssociationId.dealerId = :dealerId " +
            "group by d.dealerAssociationId.carTypeId")
    List<CarType> getSupportedCarTypesOfDealer(@Param("dealerId") long dealerId);

    @Query("select d.productType from DealerCarProduct d " +
            "where d.dealerAssociationId.dealerId = :dealerId " +
            "group by d.dealerAssociationId.productTypeId")
    List<ProductType> getSupportedProductTypesOfDealer(@Param("dealerId") long dealerId);
}