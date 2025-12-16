package com.example.websiteauto.entity.enums;

import com.example.websiteauto.entity.converters.ConvertableEnum;

public enum EngineType implements ConvertableEnum {
    NONE(""),
    GASOLINE("бензин"),
    GAS("газ"),
    GAS_GASOLINE("газ/бензин"),
    DIESEL("дизель"),
    ELECTRIC("электро");

    private final String value;

    EngineType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
