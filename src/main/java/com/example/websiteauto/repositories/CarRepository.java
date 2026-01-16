package com.example.websiteauto.repositories;

import com.example.websiteauto.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    @Query("SELECT DISTINCT c.brand FROM Car c")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT c.model FROM Car c")
    List<String> findDistinctModels();

    @Query("SELECT DISTINCT c.yearLow FROM Car c WHERE c.yearLow IS NOT NULL ORDER BY c.yearLow DESC")
    List<Integer> findDistinctYears();

    @Query("SELECT min(c.yearLow), max(c.yearUpp) FROM Car c WHERE c.brand = :brand AND c.model = :model")
    List<Object[]> findYearRangeByModel(@Param("brand") String brand, @Param("model") String model);

    @Query("SELECT DISTINCT c.model FROM Car c WHERE c.brand = :brand ORDER BY c.model")
    List<String> findModelsByBrand(@Param("brand") String brand);
}
