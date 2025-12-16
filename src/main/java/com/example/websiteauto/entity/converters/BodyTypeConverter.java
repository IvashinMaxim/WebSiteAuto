package com.example.websiteauto.entity.converters;

import com.example.websiteauto.entity.enums.BodyType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BodyTypeConverter extends GenericEnumConverter<BodyType> {
}
