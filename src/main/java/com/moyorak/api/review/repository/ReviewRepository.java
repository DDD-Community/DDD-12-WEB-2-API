package com.moyorak.api.review.repository;

import com.moyorak.api.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(
            value =
                    """
                SELECT DISTINCT r
                FROM Review r
                LEFT JOIN FETCH r.reviewPhotos
                JOIN FETCH r.user
                WHERE r.teamRestaurant.id = :teamRestaurantId
                  AND r.use = :use
                """,
            countQuery =
                    """
                SELECT COUNT(r)
                FROM Review r
                WHERE r.teamRestaurant.id = :teamRestaurantId
                  AND r.use = :use
                """)
    Page<Review> findPageWithPhotosAndUserByTeamRestaurantIdAndUse(
            @Param("teamRestaurantId") Long teamRestaurantId,
            @Param("use") boolean use,
            Pageable pageable);
}
