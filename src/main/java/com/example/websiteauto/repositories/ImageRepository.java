package com.example.websiteauto.repositories;

import com.example.websiteauto.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
