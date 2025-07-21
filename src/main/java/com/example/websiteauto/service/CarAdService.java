package com.example.websiteauto.service;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.dto.mapper.CarAdMapper;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.exception.CarAdNotFoundException;
import com.example.websiteauto.exception.UserNotFoundException;
import com.example.websiteauto.repositories.CarAdRepository;
import com.example.websiteauto.repositories.CarRepository;
import com.example.websiteauto.repositories.UserRepository;
import com.example.websiteauto.repositories.specification.CarAdSpecification;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CarAdService {

    private final CarAdRepository carAdRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarAdMapper carAdMapper;

    public CarAdService(CarAdRepository carAdRepository,
                        CarRepository carRepository,
                        UserRepository userRepository,
                        CarAdMapper carAdMapper) {
        this.carAdRepository = carAdRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carAdMapper = carAdMapper;
    }


    @Transactional
    public CarAdResponse createCarAd(CarAdRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException(authorId));

        Car car = new Car();
        car.setBrand(request.car().brand());
        car.setModel(request.car().model());
        car.setYear(request.car().year());
        car.setBodyType(request.car().bodyType());
        car.setEngineType(request.car().engineType());
        car.setEnginePower(request.car().enginePower());
        car.setHorsePower(request.car().horsePower());
        car.setDriveType(request.car().driveType());
        car.setColor(request.car().color());

        Car savedCar = carRepository.save(car);

        CarAd ad = new CarAd();
        ad.setTitle(request.title());
        ad.setDescription(request.description());
        ad.setCar(savedCar);
        ad.setAuthor(author);
        ad.setMileage(request.mileage());
        ad.setPrice(request.price());
        ad.setCreatedAt(LocalDateTime.now());

        CarAd savedAd = carAdRepository.save(ad);
        return carAdMapper.mapToAdResponse(savedAd);
    }

    @Transactional(readOnly = true)
    public CarAdResponse getCarAdById(Long id) {
        Optional<CarAd> optionalAd = carAdRepository.findById(id);
        if (optionalAd.isEmpty()) {
            throw new CarAdNotFoundException(id);
        }
        return carAdMapper.mapToAdResponse(optionalAd.get());
    }

    @Transactional(readOnly = true)
    public List<CarAdResponse> getAllCarAds() {
        List<CarAd> ads = carAdRepository.findAll();
        return ads.stream()
                .map(carAdMapper::mapToAdResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public CarAdResponse updateCarAd(Long id, CarAdRequest request) {
        Optional<CarAd> optionalAd = carAdRepository.findById(id);
        if (optionalAd.isEmpty()) {
            throw new CarAdNotFoundException(id);
        }

        CarAd ad = optionalAd.get();
        ad.setTitle(request.title());
        ad.setDescription(request.description());
        ad.setMileage(request.mileage());
        ad.setPrice(request.price());

        Car car = ad.getCar();
        car.setBrand(request.car().brand());
        car.setModel(request.car().model());
        car.setYear(request.car().year());
        car.setBodyType(request.car().bodyType());
        car.setEngineType(request.car().engineType());
        car.setEnginePower(request.car().enginePower());
        car.setHorsePower(request.car().horsePower());
        car.setDriveType(request.car().driveType());
        car.setColor(request.car().color());

        CarAd updatedAd = carAdRepository.save(ad);
        return carAdMapper.mapToAdResponse(updatedAd);
    }

    @Transactional
    public void deleteCarAd(Long id) {
        carAdRepository.deleteById(id);
    }

    public List<CarAdResponse> search(CarAdFilter filter) {
        Specification<CarAd> spec = CarAdSpecification.withFilter(filter);
        List<CarAd> ads = carAdRepository.findAll(spec);
        return ads.stream()
                .map(carAdMapper::mapToAdResponse)
                .toList();
    }

    public List<String> getAllBrands() {
        return carAdRepository.findDistinctBrands();
    }

    public List<String> getAllModels() {
        return carAdRepository.findDistinctModels();
    }

    public List<Integer> getAllYears() {
        return carAdRepository.findDistinctYears();
    }



}
