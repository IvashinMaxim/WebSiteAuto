package com.example.websiteauto.entity.enums;

import com.example.websiteauto.entity.converters.ConvertableEnum;

public enum Transmission implements ConvertableEnum {
    NONE(""),
    CVT("CVT"),
    AUTOMATIC_GEARBOX("АКПП"),
    MANUAL_GEARBOX("МКПП"),
    REDUCER("редуктор"),
    ROBOTIC_GEARBOX("РКПП");

    private final String value;

    Transmission(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

}
