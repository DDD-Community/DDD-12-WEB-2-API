package com.moyorak.api.team.dto;

import com.moyorak.api.review.domain.ReviewPhotoPaths;
import com.moyorak.api.review.dto.ReviewWithUserProjection;
import java.util.List;
import org.springframework.data.domain.Page;

public record TeamRestaurantReviewResponse(
        Long id,
        String extraText,
        Integer score,
        Integer servingTime,
        Integer waitingTime,
        String userNickname,
        String userProfileImageUrl,
        List<String> photoUrls) {
    public static Page<TeamRestaurantReviewResponse> from(
            Page<ReviewWithUserProjection> review, ReviewPhotoPaths reviewPhotoPaths) {
        return review.map(
                r ->
                        new TeamRestaurantReviewResponse(
                                r.id(),
                                r.extraText(),
                                r.score(),
                                r.servingTime(),
                                r.waitingTime(),
                                r.name(),
                                r.profileImage(),
                                reviewPhotoPaths.getPhotoPaths(r.id())));
    }
}
