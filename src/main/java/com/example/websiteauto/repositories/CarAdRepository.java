package com.example.websiteauto.repositories;

import com.example.websiteauto.entity.CarAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarAdRepository extends JpaRepository<CarAd, Long>, JpaSpecificationExecutor<CarAd>, CarAdRepositoryCustom {

    @Query("SELECT DISTINCT ca FROM CarAd ca " +
           "LEFT JOIN FETCH ca.car " +
           "LEFT JOIN FETCH ca.author " +
           "WHERE ca.id IN :ids")
    List<CarAd> findAllByIdsWithRelations(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT ca.car.brand FROM CarAd ca")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT ca.car.model FROM CarAd ca")
    List<String> findDistinctModels();

    @Query("SELECT DISTINCT ca.car.yearLow FROM CarAd ca WHERE ca.car.yearLow IS NOT NULL ORDER BY ca.car.yearLow DESC")
    List<Integer> findDistinctYears();

    @Query("SELECT DISTINCT ca.car.model FROM CarAd ca WHERE ca.car.brand = :brand ORDER BY ca.car.model")
    List<String> findModelsByBrand(@Param("brand") String brand);
}
