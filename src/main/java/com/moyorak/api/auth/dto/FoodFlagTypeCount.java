package com.moyorak.api.auth.dto;

import com.moyorak.api.auth.domain.FoodFlagType;

public record FoodFlagTypeCount(FoodFlagType type, long count) {}
