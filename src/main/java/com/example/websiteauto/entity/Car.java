package com.example.websiteauto.entity;


import com.example.websiteauto.entity.enums.BodyType;
import com.example.websiteauto.entity.enums.DriveType;
import com.example.websiteauto.entity.enums.EngineType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue
    private int id;

    private String brand;
    private String model;
    private int year;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    private double enginePower;

    private int horsepower;

    @Enumerated(EnumType.STRING)
    private DriveType driveType;

    private String color;
}
