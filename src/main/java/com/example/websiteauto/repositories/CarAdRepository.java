package com.example.websiteauto.repositories;

import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarAdRepository extends JpaRepository<CarAd, Long> , JpaSpecificationExecutor<CarAd> {

    @Query("SELECT DISTINCT c.car.brand FROM CarAd c WHERE c.car.brand IS NOT NULL")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT c.car.model FROM CarAd c WHERE c.car.model IS NOT NULL")
    List<String> findDistinctModels();

    @Query("SELECT DISTINCT c.car.year FROM CarAd c WHERE c.car.year IS NOT NULL")
    List<Integer> findDistinctYears();


    List<CarAd> findAllByAuthor(User user);
    Page<CarAd> findAllByAuthor(User author, Pageable pageable);
}
