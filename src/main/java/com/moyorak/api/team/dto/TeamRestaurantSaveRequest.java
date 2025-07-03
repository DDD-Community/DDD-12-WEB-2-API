package com.moyorak.api.team.dto;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.team.domain.TeamRestaurant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(title = "[팀 맛집] 팀 맛집 저장 DTO")
public record TeamRestaurantSaveRequest(
        @NotNull @Positive @Schema(description = "식당 ID", example = "1") Long restaurantId,
        @Size(max = 20, message = "한줄 소개는 {max}자 이하여야 합니다.")
                @Schema(description = "한줄 소개", example = "진짜 맛있습니다!")
                String summary) {
    public TeamRestaurant toTeamRestaurant(
            Long teamId, Restaurant restaurant, double distanceFromTeam) {
        return TeamRestaurant.create(teamId, restaurant, summary, distanceFromTeam);
    }
}
