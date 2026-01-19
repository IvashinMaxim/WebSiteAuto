package com.example.websiteauto.service;

import com.example.websiteauto.dto.CarAdFilter;
import com.example.websiteauto.dto.mapper.CarAdMapper;
import com.example.websiteauto.dto.mapper.CarMapper;
import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.request.CarDtoRequest;
import com.example.websiteauto.dto.response.CarAdListResponse;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.Car;
import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.entity.Image;
import com.example.websiteauto.entity.User;
import com.example.websiteauto.exception.CarAdNotFoundException;
import com.example.websiteauto.exception.UserNotFoundException;
import com.example.websiteauto.repositories.CarAdRepository;
import com.example.websiteauto.repositories.CarRepository;
import com.example.websiteauto.repositories.UserRepository;
import com.example.websiteauto.repositories.specification.CarAdSpecification;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CarAdService {

    private final CarService carService;
    private final CarAdRepository carAdRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarAdMapper carAdMapper;
    private final CarMapper carMapper;
    private final ImageService imageService;

    public CarAdService(CarService carService, CarAdRepository carAdRepository,
                        CarRepository carRepository,
                        UserRepository userRepository,
                        CarAdMapper carAdMapper, CarMapper carMapper, ImageService imageService) {
        this.carService = carService;
        this.carAdRepository = carAdRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carAdMapper = carAdMapper;
        this.carMapper = carMapper;
        this.imageService = imageService;
    }

    @Transactional
    public void createCarAd(CarAdRequest request, Long authorId, List<MultipartFile> images) throws IOException {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException(authorId));

        Car car = new Car();
        carMapper.updateCarFromDto(request.getCar(), car);
        car.setRealnessOfCar(false);
//        car.setYearUpp();

        carRepository.save(car);

        CarAd carAd = carAdMapper.toEntity(request);
        carAd.setCar(car);
        carAd.setAuthor(author);
        carAd.setCreatedAt(LocalDateTime.now());

        carAdRepository.save(carAd);
        if (images != null && !images.isEmpty()) {
            List<Image> imageList = imageService.createAndSaveImages(images, carAd);
            carAd.setImages(imageList);
        }
        carAdMapper.toAdResponse(carAd);
    }

    @Transactional(readOnly = true)
    public CarAdRequest getCarAdForEdit(Long adId, Long currentUserId) {
        CarAd ad = carAdRepository.findById(adId)
                .orElseThrow(() -> new CarAdNotFoundException(adId));
        if (!ad.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("У пользователя нет прав для редактирования объявления с ID: \" + adId");
        }
        return carAdMapper.toAdRequest(ad);
    }

    @Transactional(readOnly = true)
    public CarAdResponse getCarAdResponse(Long adId) {
        CarAd ad = carAdRepository.findById(adId)
                .orElseThrow(() -> new CarAdNotFoundException(adId));
        return carAdMapper.toAdResponse(ad);
    }

    @Transactional(readOnly = true)
    public CarAd getCarAdEntityById(Long id) {
        return carAdRepository.findById(id)
                .orElseThrow(() -> new CarAdNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<CarAdResponse> getAllCarAds() {
        List<CarAd> ads = carAdRepository.findAll();
        return ads.stream()
                .map(carAdMapper::toAdResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public void updateCarAd(Long adId, CarAdRequest request, List<MultipartFile> images, Long currentUserId) throws IOException {
        CarAd carAd = carAdRepository.findById(adId).orElseThrow(() -> new CarAdNotFoundException(adId));

        if (!carAd.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("У вас нет прав для редактирования этого объявления.");
        }

        Car oldCar = carAd.getCar();
        CarDtoRequest carDto = request.getCar();

        if (oldCar.getRealnessOfCar()) {
            if (carService.carChanged(oldCar, carDto)) {
                Car newFakeCar = Car.cloneAsFake(oldCar);
                carMapper.updateCarFromDto(carDto, newFakeCar);
                carRepository.save(newFakeCar);
                carAd.setCar(newFakeCar);
            }

        } else {
            carMapper.updateCarFromDto(carDto, oldCar);
        }
        carAdMapper.updateAdFromRequest(request, carAd);

        List<Long> idsToDelete = request.getRemoveImageIds();
        if (idsToDelete != null && !idsToDelete.isEmpty()) {
            carAd.getImages().removeIf(image -> {
                if (idsToDelete.contains(image.getId())) {
                    imageService.deleteImageFile();
                    return true;
                }
                return false;
            });
        }


        if (images != null && !images.isEmpty()) {
            List<Image> newImages = imageService.createAndSaveImages(images, carAd);
            carAd.getImages().addAll(newImages);
        }
        carAdMapper.toAdResponse(carAd);
    }

    @Transactional
    public void deleteCarAd(Long id, Long currentUser) throws AccessDeniedException {
        CarAd ad = carAdRepository.findById(id)
                .orElseThrow(() -> new CarAdNotFoundException(id));
        if (!ad.getAuthor().getId().equals(currentUser)) {
            throw new AccessDeniedException("Вы не можете удалить это объявление");
        }
        carAdRepository.delete(ad);
    }

    @Transactional(readOnly = true)
    public Page<CarAdListResponse> search(CarAdFilter filter, Pageable pageable) {
        Specification<CarAd> spec = CarAdSpecification.withFilter(filter);

        Page<Long> pageIds = carAdRepository.findAllIdsBySpecification(spec, pageable);

        if (pageIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Long> targetIds = pageIds.getContent();

        List<CarAd> ads = carAdRepository.findAllByIdsForList(targetIds);

        Map<Long, CarAd> adMap = ads.stream()
                .collect(Collectors.toMap(CarAd::getId, Function.identity()));

        List<CarAdListResponse> dtoList = targetIds.stream()
                .map(adMap::get)
                .filter(Objects::nonNull)
                .map(carAdMapper::toListResponse)
                .toList();

        return new PageImpl<>(dtoList, pageable, pageIds.getTotalElements());
    }

    public Page<CarAdResponse> findAdsByAuthorId(Long authorId, Pageable pageable) {
        Specification<CarAd> spec = (root, query, cb) -> cb.equal(root.get("author").get("id"), authorId);
        Page<CarAd> carAds = carAdRepository.findAll(spec, pageable);
        System.out.println("Found " + carAds.getTotalElements() + " ads for author " + authorId); // Отладка
        return carAds.map(carAdMapper::toAdResponse);
    }

    public List<Object> getDistinctValues(String target, Specification<CarAd> spec) {
        return carAdRepository.findDistinctValues(target, spec);
    }

    public Long createCarAd(@Valid CarAdRequest request, Long id) {
        return null;
    }

    public List<String> addImages(Long adId, Long userId, List<MultipartFile> images) {
        return null;
    }

    public void deleteImage(Long id, Long imageId, Long id1) {

    }

    public void updateCarAd(Long id, @Valid CarAdRequest request, Long userId) {
    }
}
