package com.example.websiteauto.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "car_ad")
public class CarAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    private User author;

    @ManyToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            optional = false
    )
    @JoinColumn(name = "car_id")
    private Car car;


    private int mileage;
    private BigDecimal price;
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "car_ad_images", joinColumns = @JoinColumn(name = "car_ad_id"))
    @Column(name = "image_path")
    private List<String> imagePaths;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
