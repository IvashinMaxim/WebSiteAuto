package com.example.websiteauto.entity.converters;

import com.example.websiteauto.entity.enums.EngineType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EngineTypeConverter extends GenericEnumConverter<EngineType> {
}
