package com.moyorak.api.team.controller;

import com.moyorak.api.team.dto.TeamRestaurantResponse;
import com.moyorak.api.team.service.TeamRestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@SecurityRequirement(name = "JWT")
@Tag(name = "[팀 식당] 팀 식당 API", description = "팀 식당 API 입니다.")
class TeamRestaurantController {

    private final TeamRestaurantService teamRestaurantService;

    @GetMapping("/{teamId}/restaurants/{teamRestaurantId}")
    @Operation(summary = "팀 맛집 상세 조회", description = "팀 맛집 상세 조회를 합니다.")
    public TeamRestaurantResponse getTeamRestaurant(
            @PathVariable @Positive final Long teamId,
            @PathVariable @Positive final Long teamRestaurantId) {
        return teamRestaurantService.getTeamRestaurant(teamId, teamRestaurantId);
    }
}
