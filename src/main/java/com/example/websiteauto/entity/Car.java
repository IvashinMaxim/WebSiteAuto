package com.example.websiteauto.entity;


import com.example.websiteauto.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer carId;

    @OneToMany(
            mappedBy = "car",
            fetch = FetchType.LAZY
    )
    private List<CarAd> carAds = new ArrayList<>();

    private String brand;
    private String model;

    @Column(name = "year_low")
    private Short yearLow;

    @Column(name = "year_upp")
    private Short yearUpp;

    private Byte generation;
    private Byte restyling;

    @Column(name = "body_type")
    private BodyType bodyType;

    @Column(name = "engine_volume")
    private BigDecimal engineVolume;
    @Column(name = "engine_power")
    private Integer enginePower;

    @Column(name = "engine_type")
    private EngineType engineType;

    private Transmission transmission;

    @Column(name = "drive_type")
    private DriveType driveType;

    @Column(name = "steering_side")
    private SteeringSide steeringSide;

    private String configuration;

    @Column(name = "config_year_low")
    private Short configYearLow;

    @Column(name = "config_year_upp")
    private Short configYearUpp;

    @Column(name = "real_car")
    private Boolean realnessOfCar;

    public void addCarAd(CarAd ad) {
        carAds.add(ad);
        ad.setCar(this);
    }

    public void removeCarAd(CarAd ad) {
        carAds.remove(ad);
        ad.setCar(null);
    }

    public static Car cloneAsFake(Car original){
        Car copy = new Car();

        copy.setBrand(original.getBrand());
        copy.setModel(original.getModel());
        copy.setYearLow(original.getYearLow());
        copy.setYearUpp(original.getYearUpp());
        copy.setGeneration(original.getGeneration());
        copy.setRestyling(original.getRestyling());
        copy.setBodyType(original.getBodyType());
        copy.setEngineVolume(original.getEngineVolume());
        copy.setEnginePower(original.getEnginePower());
        copy.setEngineType(original.getEngineType());
        copy.setTransmission(original.getTransmission());
        copy.setDriveType(original.getDriveType());
        copy.setSteeringSide(original.getSteeringSide());
        copy.setConfiguration(original.getConfiguration());
        copy.setConfigYearLow(original.getConfigYearLow());
        copy.setConfigYearUpp(original.getConfigYearUpp());

        copy.setRealnessOfCar(false);
        return copy;
    }
}
