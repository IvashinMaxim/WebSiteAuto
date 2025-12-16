package com.example.websiteauto.entity.enums;

import com.example.websiteauto.entity.converters.ConvertableEnum;

public enum BodyType implements ConvertableEnum {
    BUS("автобус"),
    FLATBED_TRUCK("бортовой грузовик"),
    SUV_3_DOOR("джип/suv 3 дв."),
    SUV_5_DOOR("джип/suv 5 дв."),
    COUPE("купе"),
    LIFTBACK("лифтбек"),
    MINIVAN("минивэн"),
    OPEN_BODY("открытый кузов"),
    PICKUP("пикап"),
    SEDAN("седан"),
    SEMI_TRUCK("седельный тягач"),
    STATION_WAGON("универсал"),
    VAN("фургон"),
    HATCHBACK_3_DOOR("хэтчбек 3 дв."),
    HATCHBACK_5_DOOR("хэтчбек 5 дв."),
    ALL_METAL_VAN("цельнометаллический фургон"),
    CHASSIS("шасси");

    private final String value;

    BodyType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
