package com.example.websiteauto.entity.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

@Converter
public abstract class GenericEnumConverter<E extends Enum<E> & ConvertableEnum> implements AttributeConverter<E, String> {
    private final Class<E> enumClass;

    @SuppressWarnings("unchecked")
    protected GenericEnumConverter() {
        this.enumClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public E convertToEntityAttribute(String dbValue) {
        if (dbValue == null || dbValue.trim().isEmpty()) {
            return null;
        }

        return Stream.of(enumClass.getEnumConstants())
                .filter(e -> e.getValue().equals(dbValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестное значение: " + dbValue));
    }
}
