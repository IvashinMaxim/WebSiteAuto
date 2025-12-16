package com.example.websiteauto.entity.converters;

import com.example.websiteauto.entity.enums.SteeringSide;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SteeringSideConverter extends GenericEnumConverter<SteeringSide> {
}
