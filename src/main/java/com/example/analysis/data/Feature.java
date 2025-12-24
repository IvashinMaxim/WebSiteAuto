package com.example.analysis.data;

public enum Feature {

    MILEAGE("Пробег"),
    ENGINE_POWER("Мощность"),
    //ENGINE_VOLUME("Объем"),
    YEAR("Возраст (год)"),
    IS_AUTOMATIC("Автомат (АКПП/CVT/РКПП)"),
    IS_4WD("Полный привод"),
    IS_LEFT_WHEEL("Левый руль"),
    IS_POPULAR_COLOR("Популярный цвет"),
    IS_RARE_COLOR("Редкий цвет"),
    IS_FIRM("Продавец: Фирма"),
    OWNER_REPUTATION("Стаж на платформе (лет)"),
    NEEDS_REPAIR("Требуется ремонт"),
    BAD_DOCS("Проблемы с документами"),
    // Регионы (сделаем основные для примера, остальные уйдут в базу)
    REGION_SFO("Регион: Сибирь"),
    REGION_DFO("Регион: Дальний Восток"),
    REGION_CFO("Регион: Центр (Москва)");

    private final String description;

    Feature(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
