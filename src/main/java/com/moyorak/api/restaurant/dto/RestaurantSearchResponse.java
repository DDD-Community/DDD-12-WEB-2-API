package com.moyorak.api.restaurant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

@Schema(title = "음식점 검색 결과 DTO")
public record RestaurantSearchResponse(
        @Schema(description = "식당 ID", example = "3") Long restaurantId,
        @Schema(description = "식당 이름", example = "김밥 천국") String restaurantName,
        @Schema(description = "도로명 주소", example = "우가우가 차차로 123") String roadAddress) {
    public static Page<RestaurantSearchResponse> from(Page<RestaurantSearchProjection> projection) {
        return projection.map(
                p ->
                        new RestaurantSearchResponse(
                                p.getRestaurantId(), p.getName(), p.getRoadAddress()));
    }
}
