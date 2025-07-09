package com.moyorak.api.team.service;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.service.UserService;
import com.moyorak.api.review.domain.Review;
import com.moyorak.api.review.dto.ReviewUserProjection;
import com.moyorak.api.review.service.ReviewService;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.dto.TeamRestaurantReviewRequest;
import com.moyorak.api.team.dto.TeamRestaurantReviewResponse;
import com.moyorak.global.domain.ListResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRestaurantReviewFacade {

    private final ReviewService reviewService;
    private final TeamRestaurantService teamRestaurantService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public ListResponse<TeamRestaurantReviewResponse> getTeamRestaurantReviews(
            Long teamId, Long teamRestaurantId, TeamRestaurantReviewRequest request) {
        final TeamRestaurant teamRestaurant =
                teamRestaurantService.validateUsableRestaurant(teamId, teamRestaurantId);
        final Page<ReviewUserProjection> reviews =
                reviewService.getPageByTeamRestaurantId(
                        teamRestaurant.getId(), request.toPageableAndDateSorted());

        List<Long> userIds = reviews.map(Review::getUserId).stream().distinct().toList();
        Map<Long, User> userMap = userService.getUsersAsMap(userIds);
        final Page<TeamRestaurantReviewResponse> teamRestaurantReviewResponses =
                TeamRestaurantReviewResponse.from(reviews, userMap);
        return ListResponse.from(teamRestaurantReviewResponses);
    }
}
