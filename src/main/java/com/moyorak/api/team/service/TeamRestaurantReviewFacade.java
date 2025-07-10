package com.moyorak.api.team.service;

import com.moyorak.api.review.domain.ReviewPhotoPaths;
import com.moyorak.api.review.dto.ReviewWithUserProjection;
import com.moyorak.api.review.service.ReviewPhotoService;
import com.moyorak.api.review.service.ReviewService;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.dto.TeamRestaurantReviewRequest;
import com.moyorak.api.team.dto.TeamRestaurantReviewResponse;
import com.moyorak.global.domain.ListRequest;
import com.moyorak.global.domain.ListResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRestaurantReviewFacade {

    private final ReviewService reviewService;
    private final TeamRestaurantService teamRestaurantService;
    private final ReviewPhotoService reviewPhotoService;

    @Transactional(readOnly = true)
    public ListResponse<TeamRestaurantReviewResponse> getTeamRestaurantReviews(
            Long teamId, Long teamRestaurantId, TeamRestaurantReviewRequest request) {
        final TeamRestaurant teamRestaurant =
                teamRestaurantService.getValidatedTeamRestaurant(teamId, teamRestaurantId);
        final Page<ReviewWithUserProjection> reviews =
                reviewService.getReviewWithUserByTeamRestaurantId(
                        teamRestaurant.getId(), request.toPageableAndDateSorted());

        // 리뷰 Id 추출
        final List<Long> reviewIds =
                reviews.getContent().stream().map(ReviewWithUserProjection::id).toList();

        // 리뷰 별 리뷰 사진들 정보 가져오기
        final ReviewPhotoPaths reviewPhotoPaths =
                reviewPhotoService.getReviewPhotoPathsGroupedByReviewId(reviewIds);

        final Page<TeamRestaurantReviewResponse> teamRestaurantReviewResponses =
                TeamRestaurantReviewResponse.from(reviews, reviewPhotoPaths);
        return ListResponse.from(teamRestaurantReviewResponses);
    }

    @Transactional(readOnly = true)
    public ListResponse<String> getTeamRestaurantReviewPhotos(
            Long teamId, Long teamRestaurantId, ListRequest request) {
        final TeamRestaurant teamRestaurant =
                teamRestaurantService.getValidatedTeamRestaurant(teamId, teamRestaurantId);
        final Page<String> reviewPhotoPaths =
                reviewPhotoService.getAllReviewPhotoPathsByTeamRestaurantId(
                        teamRestaurant.getId(), request.toPageableAndDateSorted());

        return ListResponse.from(reviewPhotoPaths);
    }
}
