package com.moyorak.api.team.dto;

import com.moyorak.api.restaurant.domain.RestaurantCategory;
import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[팀 맛집] 팀 맛집 검색 응답 DTO")
public record TeamRestaurantSearchResponse(
        @Schema(description = "팀 식당 ID", example = "3") Long teamRestaurantId,
        @Schema(description = "식당 이름", example = "김밥 천국") String restaurantName,
        @Schema(description = "식당 카테고리", example = "KOREAN") RestaurantCategory restaurantCategory,
        @Schema(description = "리뷰 평균 점수", example = "4.3") double averageReviewScore,
        @Schema(description = "리뷰 숫자", example = "50") int reviewCount,
        @Schema(description = "리뷰 이미지 path", example = "https://somepath/review.jpg")
                String reviewImagePath) {
    public static TeamRestaurantSearchResponse from(
            TeamRestaurantSearchSummary teamRestaurantSearchSummary,
            FirstReviewPhotoPath firstReviewPhotoPath) {
        return new TeamRestaurantSearchResponse(
                teamRestaurantSearchSummary.teamRestaurantId(),
                teamRestaurantSearchSummary.restaurantName(),
                teamRestaurantSearchSummary.restaurantCategory(),
                teamRestaurantSearchSummary.averageReviewScore(),
                teamRestaurantSearchSummary.reviewCount(),
                firstReviewPhotoPath.path());
    }
}
