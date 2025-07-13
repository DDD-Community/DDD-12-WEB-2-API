package com.moyorak.api.team.controller;

import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.review.dto.PhotoPath;
import com.moyorak.api.team.dto.TeamRestaurantLocationsResponse;
import com.moyorak.api.team.dto.TeamRestaurantResponse;
import com.moyorak.api.team.dto.TeamRestaurantReviewPhotoRequest;
import com.moyorak.api.team.dto.TeamRestaurantReviewRequest;
import com.moyorak.api.team.dto.TeamRestaurantReviewResponse;
import com.moyorak.api.team.dto.TeamRestaurantSaveRequest;
import com.moyorak.api.team.dto.TeamRestaurantSearchRequest;
import com.moyorak.api.team.dto.TeamRestaurantSearchResponse;
import com.moyorak.api.team.service.TeamRestaurantReviewFacade;
import com.moyorak.api.team.service.TeamRestaurantSearchFacade;
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
    private final TeamRestaurantSearchFacade teamRestaurantSearchFacade;
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

    @GetMapping("/{teamId}/restaurants/search")
    @Operation(summary = "팀 맛집 검색", description = "팀 맛집 검색을 합니다.")
    public ListResponse<TeamRestaurantSearchResponse> searchTeamRestaurants(
            @PathVariable @Positive final Long teamId,
            @Valid final TeamRestaurantSearchRequest teamRestaurantSearchRequest) {
        return teamRestaurantSearchFacade.search(teamId, teamRestaurantSearchRequest);
    }

    @GetMapping("/{teamId}/restaurants/locations")
    @Operation(summary = "팀 맛집 위도, 경도 리스트 조회", description = "팀 맛집의 모든 위치를 조회합니다.")
    public TeamRestaurantLocationsResponse getTeamRestaurantLocations(
            @PathVariable @Positive final Long teamId) {
        return teamRestaurantService.findTeamRestaurantLocations(teamId);
    }

    @GetMapping("/{teamId}/restaurants/{teamRestaurantId}/reviews")
    @Operation(summary = "팀 맛집 리뷰 조회", description = "팀 맛집 리뷰를 조회합니다.")
    public ListResponse<TeamRestaurantReviewResponse> getTeamRestaurantReviews(
            @PathVariable @Positive final Long teamId,
            @PathVariable @Positive final Long teamRestaurantId,
            @Valid final TeamRestaurantReviewRequest request) {
        return teamRestaurantReviewFacade.getTeamRestaurantReviews(
                teamId, teamRestaurantId, request);
    }

    @GetMapping("/{teamId}/restaurants/{teamRestaurantId}/reviews/photos")
    @Operation(summary = "팀 맛집 리뷰 사진 조회", description = "팀 맛집 리뷰 사진을 조회합니다.")
    public ListResponse<PhotoPath> getTeamRestaurantPhotos(
            @PathVariable @Positive final Long teamId,
            @PathVariable @Positive final Long teamRestaurantId,
            @Valid final TeamRestaurantReviewPhotoRequest request) {
        return teamRestaurantReviewFacade.getTeamRestaurantReviewPhotos(
                teamId, teamRestaurantId, request);
    }
}
