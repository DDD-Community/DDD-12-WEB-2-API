package com.moyorak.api.restaurant.domain;

import lombok.Getter;

@Getter
public enum RestaurantCategory {
    KOREAN("한식"),
    WESTERN("양식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    ASIAN("아시안"),
    SNACK("분식"),
    FAST_FOOD("패스트푸드"),
    CHICKEN_PIZZA("치킨피자"),
    ETC("기타");

    private final String description;

    RestaurantCategory(String description) {
        this.description = description;
    }
}
