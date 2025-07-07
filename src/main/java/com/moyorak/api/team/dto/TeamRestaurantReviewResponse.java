package com.moyorak.api.team.dto;

import com.moyorak.api.review.domain.Review;
import com.moyorak.api.review.domain.ReviewPhoto;
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
    public static Page<TeamRestaurantReviewResponse> from(Page<Review> review) {
        return review.map(
                r ->
                        new TeamRestaurantReviewResponse(
                                r.getId(),
                                r.getExtraText(),
                                r.getScore(),
                                r.getServingTime(),
                                r.getWaitingTime(),
                                r.getUser().getName(),
                                r.getUser().getProfileImage(),
                                r.getReviewPhotos().stream().map(ReviewPhoto::getPath).toList()));
    }
}
