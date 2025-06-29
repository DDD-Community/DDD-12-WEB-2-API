package com.moyorak.infra.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoPlace(
        @JsonProperty("place_name") String placeName,
        @JsonProperty("place_url") String placeUrl,
        @JsonProperty("address_name") String addressName,
        @JsonProperty("road_address_name") String roadAddressName,
        @JsonProperty("x") Double x,
        @JsonProperty("y") Double y) {}
