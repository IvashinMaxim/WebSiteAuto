package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.ImageDto;
import com.example.websiteauto.entity.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    default ImageDto toImageDto(Image image) {
        return new ImageDto(image.getId(), image.getImagePath());
    }
}
