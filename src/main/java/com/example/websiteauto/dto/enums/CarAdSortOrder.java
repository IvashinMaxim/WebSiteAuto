package com.example.websiteauto.dto.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum CarAdSortOrder {
    PRICE_ASC("price", Sort.Direction.ASC, "Дешевле"),
    PRICE_DESC("price", Sort.Direction.DESC, "Дороже"),
    POPULAR("views", Sort.Direction.DESC, "Популярные");

    private final String property;
    private final Sort.Direction direction;
    private final String displayValue;

    CarAdSortOrder(String property, Sort.Direction direction, String displayValue) {
        this.property = property;
        this.direction = direction;
        this.displayValue = displayValue;
    }
}
