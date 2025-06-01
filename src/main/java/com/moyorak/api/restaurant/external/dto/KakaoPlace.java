package com.moyorak.api.restaurant.external.dto;

public record KakaoPlace(
        String place_name,
        String address_name,
        String road_address_name,
        String phone,
        String x,
        String y) {}
