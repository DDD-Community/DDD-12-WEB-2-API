package com.moyorak.infra.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoPlace(
        @JsonProperty("place_name") String placeName,
        @JsonProperty("address_name") String addressName,
        @JsonProperty("road_address_name") String roadAddressName,
        @JsonProperty("phone") String phone,
        @JsonProperty("x") String x,
        @JsonProperty("y") String y) {}
