package com.moyorak.api.review.service;

import com.moyorak.api.review.domain.Review;
import com.moyorak.api.review.dto.ReviewSearchRequest;
import com.moyorak.api.review.repository.ReviewRepository;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.domain.TeamRestaurantNotFoundException;
import com.moyorak.api.team.dto.TeamRestaurantReviewResponse;
import com.moyorak.api.team.repository.TeamRestaurantRepository;
import com.moyorak.global.domain.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final TeamRestaurantRepository teamRestaurantRepository;

    public ListResponse<TeamRestaurantReviewResponse> getReviews(
            Long teamId, Long teamRestaurantId, ReviewSearchRequest request) {
        final TeamRestaurant teamRestaurant =
                teamRestaurantRepository
                        .findByTeamIdAndIdAndUse(teamId, teamRestaurantId, true)
                        .orElseThrow(TeamRestaurantNotFoundException::new);
        final Page<Review> reviews =
                reviewRepository.findPageWithPhotosAndUserByTeamRestaurantIdAndUse(
                        teamRestaurant.getId(), true, request.toPageableAndDateSorted());
        final Page<TeamRestaurantReviewResponse> teamRestaurantReviewResponses =
                TeamRestaurantReviewResponse.from(reviews);
        return ListResponse.from(teamRestaurantReviewResponses);
    }
}
