package com.moyorak.api.restaurant.dto;

import com.moyorak.infra.client.kakao.dto.KakaoPlace;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "음식점 정보 DTO")
public record ExternalRestaurantSearchResponse(
        @Schema(description = "장소명", example = "곱창고 을지로점") String name,
        @Schema(description = "장소 url", example = "http://place.map.kakao.com/26338954")
                String placeUrl,
        @Schema(description = "전체 지번 주소", example = "서울 중구 을지로3가 95-1") String address,
        @Schema(description = "전체 도로명 주소", example = "서울 중구 을지로 108") String roadAddress,
        @Schema(description = "경도", example = "126.990774") Double longitude,
        @Schema(description = "위도", example = "37.566345") Double latitude) {
    public static ExternalRestaurantSearchResponse fromKakaoPlace(KakaoPlace kakaoPlace) {
        return new ExternalRestaurantSearchResponse(
                kakaoPlace.placeName(),
                kakaoPlace.placeUrl(),
                kakaoPlace.addressName(),
                kakaoPlace.roadAddressName(),
                kakaoPlace.x(),
                kakaoPlace.y());
    }
}
