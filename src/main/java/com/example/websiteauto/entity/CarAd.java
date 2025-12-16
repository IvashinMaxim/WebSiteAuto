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
@Table(name = "car_ads")
public class CarAd {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private BigDecimal price;
    private Integer views;
    private Integer mileage;
    private String color;
    private String city;
    private String region;
    @Column(name = "macroregion")
    private String macroRegion;
    private String notes;
    private String owner;
    @Column(name = "owners")
    private String numberOfOwners;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;


    @OneToMany(
            mappedBy = "carAd",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Image> images;

    public void addImage(Image image) {
        images.add(image);
        image.setCarAd(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setCarAd(null);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
