package com.moyorak.api.restaurant.dto;

import com.moyorak.infra.client.kakao.dto.KakaoPlace;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "음식점 정보 DTO")
public record ExternalRestaurantSearchResponse(
        @Schema(description = "장소명", example = "곱창고 을지로점") String placeName,
        @Schema(description = "전체 지번 주소", example = "서울 중구 을지로3가 95-1") String addressName,
        @Schema(description = "전체 도로명 주소", example = "서울 중구 을지로 108") String roadAddressName,
        @Schema(description = "전화번호", example = "02-1234-5678") String phone,
        @Schema(description = "경도(Longitude, X좌표)", example = "126.990774") String x,
        @Schema(description = "위도(Latitude, Y좌표)", example = "37.566345") String y) {

    public static ExternalRestaurantSearchResponse fromKakaoPlace(KakaoPlace kakaoPlace) {
        return new ExternalRestaurantSearchResponse(
                kakaoPlace.placeName(),
                kakaoPlace.addressName(),
                kakaoPlace.roadAddressName(),
                kakaoPlace.phone(),
                kakaoPlace.x(),
                kakaoPlace.y());
    }
}
