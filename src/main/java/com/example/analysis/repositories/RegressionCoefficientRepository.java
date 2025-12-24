package com.example.analysis.repositories;


import com.example.analysis.entity.RegressionCoefficient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegressionCoefficientRepository extends JpaRepository<RegressionCoefficient, Long> {
}
