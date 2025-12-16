package com.example.websiteauto.entity.enums;

import com.example.websiteauto.entity.converters.ConvertableEnum;

public enum SteeringSide implements ConvertableEnum {
    NONE(""),
    LEFT("левый"),
    RIGHT("правый");

    private final String value;

    SteeringSide(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
