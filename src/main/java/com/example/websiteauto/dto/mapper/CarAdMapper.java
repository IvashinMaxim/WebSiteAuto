package com.example.websiteauto.dto.mapper;

import com.example.websiteauto.dto.request.CarAdRequest;
import com.example.websiteauto.dto.response.CarAdListResponse;
import com.example.websiteauto.dto.response.CarAdResponse;
import com.example.websiteauto.entity.CarAd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CarMapper.class})
public interface CarAdMapper {
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(target = "images", source = "images")
    CarAdResponse toAdResponse(CarAd carAd);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorUsername", source = "author.username")
    CarAdListResponse toListResponse(CarAd carAd);

    @Mapping(target = "car", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "id", ignore = true)
    CarAd toEntity(CarAdRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateAdFromRequest(CarAdRequest request, @MappingTarget CarAd carAd);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "car", source = "car")
    @Mapping(target = "removeImageIds", ignore = true)
    CarAdRequest toAdRequest(CarAd carAd);
}
