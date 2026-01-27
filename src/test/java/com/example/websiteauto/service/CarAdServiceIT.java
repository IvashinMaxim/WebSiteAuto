package com.example.websiteauto.service;

import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.request.CarDtoRequest;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.repositories.CarAdRepository;
import com.example.websiteauto.repositories.CarRepository;
import com.example.websiteauto.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest

@ActiveProfiles("test")
public class CarAdServiceIT {

    @Autowired
    private CarAdService carAdService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarAdRepository carAdRepository;

    @Autowired
    private CarRepository carRepository;

    @MockitoBean
    private ImageService imageService;

    private User savedUser;
    private CarAdRequest validRequest;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("securePassword123");
        user.setEmail("test@example.com");

        savedUser = userRepository.save(user);

        CarDtoRequest carDto = new CarDtoRequest();
        carDto.setBrand("Toyota");
        carDto.setModel("Camry");
        carDto.setYearLow(2020);
        carDto.setBodyType("седан");

        carDto.setEngineVolume(new BigDecimal("2.5"));
        carDto.setEnginePower(181);
        carDto.setEngineType("бензин");
        carDto.setTransmission("АКПП");
        carDto.setDriveType("4WD");
        carDto.setSteeringSide("левый");

        validRequest = new CarAdRequest();
        validRequest.setCar(carDto);

        validRequest.setPrice(new BigDecimal("3500000.00"));
        validRequest.setMileage(45000);
        validRequest.setColor("Black");
        validRequest.setCity("Moscow");
        validRequest.setRegion("Moscow Region");
    }

    @AfterEach
    void clean() {
        carAdRepository.deleteAll();
        carRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void createCarAd_persistsEntities_success() throws IOException {
        when(imageService.createAndSaveImages(any(), any())).thenReturn(List.of());

        carAdService.createCarAd(validRequest, savedUser.getId(), null);

        List<CarAd> ads = carAdRepository.findByAuthorId(savedUser.getId());

        assertEquals(1, ads.size());
        assertNotNull(ads.get(0).getCar().getCarId());
    }


    @Test
    void createCarAd_rollbackOnException() throws IOException {
        List<MultipartFile> images = List.of(mock(MultipartFile.class));

        doThrow(new RuntimeException("Storage failure"))
                .when(imageService).createAndSaveImages(anyList(), any(CarAd.class));

        assertThrows(
                RuntimeException.class,
                () -> carAdService.createCarAd(validRequest, savedUser.getId(), images)
        );

        assertEquals(0, carAdRepository.count(), "Таблица CarAd должна быть пуста после отката");
        assertEquals(0, carRepository.count(), "Таблица Car должна быть пуста после отката");
    }
}