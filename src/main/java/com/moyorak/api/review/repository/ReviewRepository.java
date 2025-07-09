package com.moyorak.api.review.repository;

import com.moyorak.api.review.domain.Review;
import com.moyorak.api.review.dto.ReviewUserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //    Page<Review> findByTeamRestaurantIdAndUse(Long teamRestaurantId, boolean use, Pageable
    // pageable);

    @Query(
            """
    SELECT new com.moyorak.api.review.dto.ReviewUserProjection(
        r.id, r.extraText,r.score, r.servingTime, r.waitingTime,
        u.name, u.profileImage
    )
    FROM Review r
    JOIN User u ON r.userId = u.id
    WHERE r.teamRestaurantId = :teamRestaurantId AND r.use = true
""")
    Page<ReviewUserProjection> findReviewWithUserByTeamRestaurantId(
            @Param("teamRestaurantId") Long teamRestaurantId, Pageable pageable);
}
