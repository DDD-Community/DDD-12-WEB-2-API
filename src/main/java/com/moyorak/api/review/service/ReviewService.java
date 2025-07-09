package com.moyorak.api.review.service;

import com.moyorak.api.review.dto.ReviewWithUserProjection;
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

    @Transactional(readOnly = true)
    public Page<ReviewWithUserProjection> getReviewWithUserByTeamRestaurantId(
            Long teamRestaurantId, Pageable pageable) {
        return reviewRepository.findReviewWithUserByTeamRestaurantId(teamRestaurantId, pageable);
    }
}
