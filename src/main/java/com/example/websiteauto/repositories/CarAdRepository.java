package com.example.websiteauto.repositories;

import com.example.websiteauto.dto.CarRegressionRow;
import com.example.websiteauto.dto.response.CarAdListResponse;
import com.example.websiteauto.entity.CarAd;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarAdRepository extends JpaRepository<CarAd, Long>, JpaSpecificationExecutor<CarAd>, CarAdRepositoryCustom {

    List<CarAd> findByAuthorId(Long authorId);

    @Query("SELECT DISTINCT ca FROM CarAd ca " +
           "LEFT JOIN FETCH ca.car " +
           "LEFT JOIN FETCH ca.author " +
           "LEFT join fetch ca.images " +
           "WHERE ca.id IN :ids")
    List<CarAd> findAllByIdsWithRelations(@Param("ids") List<Long> ids);

    @EntityGraph(attributePaths = {"car", "author"})
    @Query("SELECT ca FROM CarAd ca " +
           "LEFT JOIN FETCH ca.car " +
           "WHERE ca.id IN :ids")
    List<CarAd> findAllByIdsForList(@Param("ids") List<Long> ids);

    @Query("""
                SELECT new com.example.websiteauto.dto.CarRegressionRow(
                    ca.price,
                    ca.mileage,
                    c.enginePower,
            
                    (c.configYearLow + c.configYearUpp) * 1.0 / 2,
                    c.bodyType,
                    ca.color,          
                    ca.owner,          
                    ca.notes,          
                    c.transmission,    
                    c.driveType,       
                    c.steeringSide,    
                    ca.macroRegion,    
                    ca.city            
                )
                FROM CarAd ca
                JOIN ca.car c
                WHERE ca.price BETWEEN 200000 AND 15000000
                  AND ca.mileage>10000
                  AND c.enginePower IS NOT NULL
            
                  AND c.configYearLow IS NOT NULL
                  AND c.configYearUpp IS NOT NULL
                  AND c.realnessOfCar IS TRUE
                  AND ca.createdAt >= :fromDate
            """)
    List<CarRegressionRow> findDataForRegression(@Param("fromDate") LocalDateTime fromDate, Pageable pageable);

}
