package com.moyorak.api.team.dto;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.review.domain.Review;
import com.moyorak.api.review.domain.ReviewPhoto;
import java.util.List;
import java.util.Map;
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
            Page<Review> review, Map<Long, User> userMap) {
        return review.map(
                r ->
                        new TeamRestaurantReviewResponse(
                                r.getId(),
                                r.getExtraText(),
                                r.getScore(),
                                r.getServingTime(),
                                r.getWaitingTime(),
                                userMap.get(r.getUserId()).getName(),
                                userMap.get(r.getUserId()).getProfileImage(),
                                r.getReviewPhotos().stream().map(ReviewPhoto::getPath).toList()));
    }
}
