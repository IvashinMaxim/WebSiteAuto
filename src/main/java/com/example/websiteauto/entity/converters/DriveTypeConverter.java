package com.example.websiteauto.entity.converters;

import com.example.websiteauto.entity.enums.DriveType;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DriveTypeConverter extends GenericEnumConverter<DriveType> {
}