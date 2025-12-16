package com.example.websiteauto.entity.enums;

import com.example.websiteauto.entity.converters.ConvertableEnum;

public enum DriveType implements ConvertableEnum {
    NONE(""),
    AWD("4WD"), // AWD (All-Wheel Drive)
    MID("двигатель посередине (MID)"),
    RWD("задний"), // RWD (Rear-Wheel Drive)
    FWD("передний"); // FWD (Front-Wheel Drive)

    private final String value;

    DriveType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
