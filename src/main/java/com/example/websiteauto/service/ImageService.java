package com.example.websiteauto.service;

import com.example.websiteauto.entity.CarAd;
import com.example.websiteauto.entity.Image;
import com.example.websiteauto.exception.ImageNotFoundException;
import com.example.websiteauto.repositories.ImageRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;
    @Value("${file.upload.dir}")
    private String uploadDir;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @PostConstruct
    public void init() {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
                log.info("Директория для загрузки файлов создана: {}", uploadDir);
            } catch (IOException e) {
                log.error("КРИТИЧЕСКАЯ ОШИБКА: Не удалось создать директорию загрузки: {}", uploadDir, e);
                throw new RuntimeException("Не удалось инициализировать директорию для загрузки файлов.", e);
            }
        } else {
            log.info("Директория для загрузки файлов уже существует: {}", uploadDir);
        }
    }

    @Transactional
    public List<Image> createAndSaveImages(List<MultipartFile> images, CarAd carAd) throws IOException {
        List<Image> imageList = new ArrayList<>();

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
                String safeFilename = Paths.get(originalFilename).getFileName().toString();
                String fileName = UUID.randomUUID().toString() + "_" + safeFilename;
                Path filePath = Paths.get(uploadDir, fileName);

                try {
                    file.transferTo(filePath.toFile());
                    if (TransactionSynchronizationManager.isSynchronizationActive()) {
                        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                            @Override
                            public void afterCompletion(int status) {
                                if (status == STATUS_ROLLED_BACK) {
                                    log.warn("Транзакция откатилась. Удаляем мусорный файл: {}", fileName);
                                    try {
                                        Files.deleteIfExists(filePath);
                                    } catch (IOException e) {
                                        log.error("Не удалось удалить файл при откате: {}", fileName, e);
                                    }
                                }
                            }
                        });
                    }
                    Image image = new Image();
                    image.setImagePath(fileName);
                    image.setCarAd(carAd);
                    Image savedImage = imageRepository.save(image);
                    imageList.add(savedImage);

                    log.info("Файл успешно загружен и привязан к транзакции: {}", fileName);

                } catch (IOException e) {
                    log.error("Ошибка ввода-вывода: {}", fileName, e);
                    throw new IOException("Ошибка загрузки файла", e);
                }
            }
        }
        return imageList;
    }

    public void deleteImageById(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageNotFoundException("Изображение не найдено " + imageId));
        String fileName = image.getImagePath();
        imageRepository.deleteById(imageId);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    deletePhysicalFile(fileName);
                }
            });
        } else {
            deletePhysicalFile(fileName);
        }
    }

    private void deletePhysicalFile(String fileName) {
        Path filePath = Paths.get(uploadDir, fileName);
        try {
            if (Files.deleteIfExists(filePath)) {
                log.info("Файл {} успешно удален", fileName);
            }
        } catch (IOException e) {
            log.error("Ошибка при удалении файла: {}", fileName, e);
        }
    }
}
