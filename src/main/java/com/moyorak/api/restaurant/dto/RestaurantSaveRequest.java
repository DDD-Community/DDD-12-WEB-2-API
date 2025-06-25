package com.moyorak.api.restaurant.dto;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.restaurant.domain.RestaurantCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(title = "[식당] 모여락 DB에 식당 저장 DTO")
public record RestaurantSaveRequest(
        @Size(max = 512, message = "url은 {max}자 이하여야 합니다.")
                @Schema(description = "식당 외부 링크", example = "http://place.map.kakao.com/26338954")
                String placeUrl,
        @NotBlank(message = "식당 이름을 입력해주세요.")
                @Size(max = 255, message = "식당 이름은 {max}자 이하여야 합니다.")
                @Schema(description = "식당 이름", example = "우가우가 차차차")
                String name,
        @NotBlank(message = "식당 지번 주소를 입력해주세요.")
                @Size(max = 255, message = "식당 지번 주소는 {max}자 이하여야 합니다.")
                @Schema(description = "식당 지번 주소", example = "우가우가시 차차차동 24번길")
                String address,
        @NotBlank(message = "식당 도로명 주소를 입력해주세요.")
                @Size(max = 255, message = "식당 도로명 주소는 {max}자 이하여야 합니다.")
                @Schema(description = "식당 도로명 주소", example = "우가우가 차차로 1234")
                String roadAddress,
        @NotNull(message = "식당 카테고리를 선택해주세요.") @Schema(description = "식당 카테고리", example = "KOREAN")
                RestaurantCategory category,
        @NotNull(message = "경도를 입력해주세요.")
                @DecimalMin(value = "-180.0", message = "경도는 {value} 이상이어야 합니다.")
                @DecimalMax(value = "180.0", message = "경도는 {value} 이하여야 합니다.")
                @Schema(description = "경도", example = "127.043616")
                Double longitude,
        @NotNull(message = "위도를 입력해주세요.")
                @DecimalMin(value = "-90.0", message = "위도는 {value} 이상이어야 합니다.")
                @DecimalMax(value = "90.0", message = "위도는 {value} 이하여야 합니다.")
                @Schema(description = "위도", example = "37.503095")
                Double latitude) {
    public Restaurant toRestaurant() {
        return Restaurant.create(
                placeUrl, name, address, roadAddress, category, longitude, latitude);
    }
}
