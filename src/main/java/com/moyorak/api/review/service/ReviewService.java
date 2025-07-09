package com.moyorak.api.review.service;

import com.moyorak.api.review.dto.ReviewUserProjection;
import com.moyorak.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    //    @Transactional(readOnly = true)
    //    public Page<Review> getPageByTeamRestaurantId(Long teamRestaurantId, Pageable pageable) {
    //        return reviewRepository.findByTeamRestaurantIdAndUse(
    //                teamRestaurantId, true, pageable);
    //    }

    @Transactional(readOnly = true)
    public Page<ReviewUserProjection> getPageByTeamRestaurantId(
            Long teamRestaurantId, Pageable pageable) {
        return reviewRepository.findReviewWithUserByTeamRestaurantId(teamRestaurantId, pageable);
    }
}
