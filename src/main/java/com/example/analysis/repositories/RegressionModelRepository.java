package com.example.analysis.repositories;

import com.example.analysis.entity.RegressionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegressionModelRepository extends JpaRepository<RegressionModel, Long> {
}
