package com.example.websiteauto.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "carAd")
public class CarAd {
    @Id
    @GeneratedValue
    private int id;

    private String title;
    private String description;

    @ManyToOne
    private User author;

    @ManyToOne(optional = false,cascade = CascadeType.ALL)
    private Car car;

    private int mileage;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
