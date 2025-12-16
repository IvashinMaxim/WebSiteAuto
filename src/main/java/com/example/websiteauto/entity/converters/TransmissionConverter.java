package com.example.websiteauto.entity.converters;

import com.example.websiteauto.entity.enums.Transmission;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransmissionConverter extends GenericEnumConverter<Transmission> {
}
