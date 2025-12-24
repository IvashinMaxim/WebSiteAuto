package com.example.analysis.entity;

import com.example.analysis.data.MarketSegment;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "regression_models")
@Data
public class RegressionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MarketSegment segment;

    private String target; // "price"

    private Double r2;

    private Integer observationsCount;

    private LocalDateTime trainedAt;

    private String comment;
}

