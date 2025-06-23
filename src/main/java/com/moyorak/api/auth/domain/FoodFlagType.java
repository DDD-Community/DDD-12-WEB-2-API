package com.moyorak.api.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FoodFlagType {
    DISLIKE("비선호 음식"),
    ALLERGY("알러지");

    private final String description;
}
