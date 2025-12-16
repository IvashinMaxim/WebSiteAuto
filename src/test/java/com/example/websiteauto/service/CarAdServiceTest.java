//package com.example.websiteauto.service;
//
//import com.example.websiteauto.dto.mapper.CarAdMapper;
//import com.example.websiteauto.dto.request.CarAdRequest;
//import com.example.websiteauto.dto.response.CarAdResponse;
//import com.example.websiteauto.entity.Car;
//import com.example.websiteauto.entity.CarAd;
//import com.example.websiteauto.entity.User;
//import com.example.websiteauto.entity.enums.BodyType;
//import com.example.websiteauto.entity.enums.DriveType;
//import com.example.websiteauto.entity.enums.EngineType;
//import com.example.websiteauto.exception.UserNotFoundException;
//import com.example.websiteauto.repositories.CarAdRepository;
//
//import com.example.websiteauto.repositories.CarRepository;
//import com.example.websiteauto.repositories.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.nio.file.AccessDeniedException;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CarAdServiceTest {
//
//    @Mock
//    private CarAdRepository carAdRepository;
//
//    @Mock
//    private CarAdMapper carAdMapper;
//
//    @Mock
//    private CarRepository carRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private CarAdService carAdService;
//
//    private CarAdRequest request;
//    private User author;
//    private Car mappedCar;
//    private CarAd mappedAd;
//    private CarAdResponse response;
//
//    @BeforeEach
//    void setUp() {
//        CarDto car = new CarDto("Test Brand", "Test Model", 2020, BodyType.SEDAN, EngineType.DIESEL, 150, 200, DriveType.ALL, "Black");
//        request = new CarAdRequest("Test Title", "Test Description",
//                car,
//                10000, new BigDecimal(50000));
//
//        author = new User();
//        author.setId(1L);
//
//        mappedCar = new Car();
//        mappedCar.setId(10L);
//
//        mappedAd = new CarAd();
//        mappedAd.setId(20L);
//
//        CarAdResponse response = new CarAdResponse(
//                20L,
//                "Test Title",
//                "Test Description",
//                car,
//                10000,
//                new BigDecimal(50000),
//                LocalDateTime.now(),
//                1L,
//                "testuser",
//                Collections.singletonList("path/to/image.jpg")
//        );
//    }
//
//    @Test
//    void createCarAd_shouldCreateAdSuccessfully() throws IOException {
//        Long authorId = 1L;
//        User author = new User();
//        author.setId(authorId);
//
//        Car mappedCar = new Car();
//        mappedCar.setId(10L);
//
//        CarAd mappedAd = new CarAd();
//        mappedAd.setId(20L);
//
//        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
//        when(carAdMapper.toCar(any(CarDto.class))).thenReturn(mappedCar);
//        when(carRepository.save(any(Car.class))).thenReturn(mappedCar);
//        when(carAdMapper.toEntity(any(CarAdRequest.class))).thenReturn(mappedAd);
//        when(carAdRepository.save(any(CarAd.class))).thenReturn(mappedAd);
//        when(carAdService.saveImages(anyList())).thenReturn(Collections.singletonList("path/to/image.jpg"));
//        when(carAdMapper.toAdResponse(any(CarAd.class))).thenReturn(response);
//
//        CarAdResponse response = carAdService.createCarAd(request, authorId, Collections.emptyList());
//
//        verify(userRepository, times(1)).findById(authorId);
//        verify(carRepository, times(1)).save(any(Car.class));
//        verify(carAdRepository, times(1)).save(any(CarAd.class));
//        verify(carAdService, times(1)).saveImages(anyList());
//        verify(carAdMapper, times(1)).toAdResponse(any(CarAd.class));
//
//        assertNotNull(response);
//    }
//
//    @Test
//    void createCarAd_shouldThrowException_whenAuthorNotFound() {
//        Long authorId = 99L;
//        when(userRepository.findById(authorId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () ->
//                carAdService.createCarAd(request, authorId, Collections.emptyList())
//        );
//    }
//
//    @Test
//    void deleteCarAd_shouldThrowException_whenUserIsNotAuthor() {
//        Long adId = 1L;
//        Long currentUserId = 2L;
//
//        User author = new User();
//        author.setId(1L);
//
//        CarAd ad = new CarAd();
//        ad.setAuthor(author);
//
//        when(carAdRepository.findById(adId)).thenReturn(Optional.of(ad));
//
//        assertThrows(AccessDeniedException.class, () ->
//                carAdService.deleteCarAd(adId, currentUserId)
//        );
//    }
//}