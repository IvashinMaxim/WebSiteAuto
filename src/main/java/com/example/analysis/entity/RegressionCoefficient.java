package com.example.analysis.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "regression_coefficients")
@Data
public class RegressionCoefficient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private RegressionModel model;

    private String feature;   // mileage, enginePower, year, intercept
    private Double value;
}

