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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CarAdService {

    private final CarAdRepository carAdRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarAdMapper carAdMapper;
    private final String uploadDir;

    public CarAdService(CarAdRepository carAdRepository,
                        CarRepository carRepository,
                        UserRepository userRepository,
                        CarAdMapper carAdMapper) {
        this.carAdRepository = carAdRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carAdMapper = carAdMapper;
        this.uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/images/";
    }

    @Transactional
    public CarAdResponse createCarAd(CarAdRequest request, Long authorId, List<MultipartFile> images) throws IOException {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException(authorId));

        Car car = carAdMapper.toCar(request.car());
        Car savedCar = carRepository.save(car);
        CarAd carAd = carAdMapper.toEntity(request);

        carAd.setCar(savedCar);
        carAd.setAuthor(author);
        carAd.setCreatedAt(LocalDateTime.now());
        carAd.setImagePaths(saveImages(images));

        carAdRepository.save(carAd);
        return carAdMapper.toAdResponse(carAd);
    }

    @Transactional
    public CarAdRequest getCarAdForEdit(Long adId, Long currentUserId) throws AccessDeniedException {
        CarAd ad = carAdRepository.findById(adId)
                .orElseThrow(() -> new CarAdNotFoundException(adId));
        if (!ad.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("У вас нет прав для редактирования этого объявления.");
        }
        return carAdMapper.toRequest(ad);
    }

    @Transactional(readOnly = true)
    public CarAd getCarAdEntityById(Long id) {
        return carAdRepository.findById(id)
                .orElseThrow(() -> new CarAdNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public CarAdResponse getCarAdResponseById(Long id) {
        CarAd ad = getCarAdEntityById(id);
        return carAdMapper.toAdResponse(ad);
    }


    @Transactional(readOnly = true)
    public List<CarAdResponse> getAllCarAds() {
        List<CarAd> ads = carAdRepository.findAll();
        return ads.stream()
                .map(carAdMapper::toAdResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public CarAdResponse updateCarAd(Long id, CarAdRequest request, List<MultipartFile> images, Long currentUserId) throws IOException {
        CarAd carAd = carAdRepository.findById(id).orElseThrow(() -> new CarAdNotFoundException(id));

        if (!carAd.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("У вас нет прав для редактирования этого объявления.");
        }

        carAd.setTitle(request.title());
        carAd.setDescription(request.description());
        carAd.setMileage(request.mileage());
        carAd.setPrice(request.price());

        Car car = carAd.getCar();
        carAdMapper.updateCarFromDto(request.car(), car);

        if (images != null && !images.isEmpty()) {
            carAd.setImagePaths(saveImages(images));
        }
        return carAdMapper.toAdResponse(carAd);
    }

    private List<String> saveImages(List<MultipartFile> images) throws IOException {
        List<String> imagePaths = new ArrayList<>();
        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                    Path path = Paths.get(uploadDir, fileName);
                    Files.createDirectories(path.getParent());
                    image.transferTo(path.toFile());
                    imagePaths.add("/uploads/" + fileName);
                }
            }
        }
        return imagePaths;
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
    public Page<CarAdResponse> search(CarAdFilter filter, Pageable pageable) {
        Specification<CarAd> spec = CarAdSpecification.withFilter(filter);
        return carAdRepository.findAll(spec, pageable)
                .map(carAdMapper::toAdResponse);
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

    public Page<CarAdResponse> findAdsByAuthorId(Long authorId, Pageable pageable) {
        Specification<CarAd> spec = (root, query, cb) -> cb.equal(root.get("author").get("id"), authorId);
        Page<CarAd> carAds = carAdRepository.findAll(spec, pageable);
        System.out.println("Found " + carAds.getTotalElements() + " ads for author " + authorId); // Отладка
        return carAds.map(carAdMapper::toAdResponse);
    }
}
