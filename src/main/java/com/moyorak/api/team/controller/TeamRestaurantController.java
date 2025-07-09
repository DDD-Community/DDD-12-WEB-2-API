package com.moyorak.api.team.controller;

import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.team.dto.TeamRestaurantResponse;
import com.moyorak.api.team.dto.TeamRestaurantReviewRequest;
import com.moyorak.api.team.dto.TeamRestaurantReviewResponse;
import com.moyorak.api.team.dto.TeamRestaurantSaveRequest;
import com.moyorak.api.team.service.TeamRestaurantReviewFacade;
import com.moyorak.api.team.service.TeamRestaurantService;
import com.moyorak.global.domain.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@SecurityRequirement(name = "JWT")
@Tag(name = "[팀 식당] 팀 식당 API", description = "팀 식당 API 입니다.")
class TeamRestaurantController {

    private final TeamRestaurantService teamRestaurantService;
    private final TeamRestaurantReviewFacade teamRestaurantReviewFacade;

    @GetMapping("/{teamId}/restaurants/{teamRestaurantId}")
    @Operation(summary = "팀 맛집 상세 조회", description = "팀 맛집 상세 조회를 합니다.")
    public TeamRestaurantResponse getTeamRestaurant(
            @PathVariable @Positive final Long teamId,
            @PathVariable @Positive final Long teamRestaurantId) {
        return teamRestaurantService.getTeamRestaurant(teamId, teamRestaurantId);
    }

    @PostMapping("/{teamId}/restaurants")
    @Operation(summary = "팀 맛집 등록", description = "팀 맛집을 등록합니다.")
    public void saveTeamRestaurant(
            @Valid @RequestBody final TeamRestaurantSaveRequest teamRestaurantSaveRequest,
            @PathVariable @Positive final Long teamId,
            @AuthenticationPrincipal final UserPrincipal userPrincipal) {
        teamRestaurantService.save(userPrincipal.getId(), teamId, teamRestaurantSaveRequest);
    }

    @GetMapping("/{teamId}/restaurants/{teamRestaurantId}/reviews")
    @Operation(summary = "팀 맛집 리뷰 조회", description = "팀 맛집 리뷰를 조히해 옵니다.")
    public ListResponse<TeamRestaurantReviewResponse> getTeamRestaurantReviews(
            @PathVariable @Positive final Long teamId,
            @PathVariable @Positive final Long teamRestaurantId,
            @Valid final TeamRestaurantReviewRequest request) {
        return teamRestaurantReviewFacade.getTeamRestaurantReviews(
                teamId, teamRestaurantId, request);
    }
}
