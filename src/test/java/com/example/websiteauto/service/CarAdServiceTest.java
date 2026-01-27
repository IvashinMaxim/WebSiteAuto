package com.example.websiteauto.service;

import com.example.websiteauto.dto.mapper.CarAdMapper;
import com.example.websiteauto.dto.mapper.CarMapper;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.request.CarDtoRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.entity.Image;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.entity.enums.BodyType;
import com.example.websiteauto.entity.enums.DriveType;
import com.example.websiteauto.entity.enums.EngineType;
import com.example.websiteauto.exception.UserNotFoundException;
import com.example.websiteauto.repositories.CarAdRepository;

import com.example.websiteauto.repositories.CarRepository;
import com.example.websiteauto.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarAdServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarAdRepository carAdRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private CarMapper carMapper;
    @Mock
    private CarAdMapper carAdMapper;

    @InjectMocks
    private CarAdService carAdService;

    private User user;
    private CarAdRequest request;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        request = new CarAdRequest();
        request.setCar(new CarDtoRequest());
        lenient().when(carAdMapper.toEntity(any())).thenReturn(new CarAd());
    }

    @Test
    void createCarAd_success_withoutImages() throws IOException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        carAdService.createCarAd(request, 1L, null);

        verify(userRepository).findById(1L);
        verify(carRepository).save(any(Car.class));
        verify(carAdRepository).save(any(CarAd.class));
        verify(imageService, never()).createAndSaveImages(any(), any());
    }

    @Test
    void createCarAd_success_withImages() throws IOException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        MultipartFile image = mock(MultipartFile.class);

        List<Image> savedImages = List.of(new Image());
        when(imageService.createAndSaveImages(any(), any())).thenReturn(savedImages);

        carAdService.createCarAd(request, 1L, List.of(image));

        verify(imageService).createAndSaveImages(any(), any());
        verify(carAdRepository).save(any(CarAd.class));
    }

    @Test
    void createCarAd_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> carAdService.createCarAd(request, 1L, null)
        );

        verify(carRepository, never()).save(any());
        verify(carAdRepository, never()).save(any());
    }

    @Test
    void createCarAd_imageServiceThrowsException() throws IOException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        MultipartFile image = mock(MultipartFile.class);

        when(imageService.createAndSaveImages(any(), any()))
                .thenThrow(new IOException("Disk error"));

        assertThrows(
                IOException.class,
                () -> carAdService.createCarAd(request, 1L, List.of(image))
        );
    }

}