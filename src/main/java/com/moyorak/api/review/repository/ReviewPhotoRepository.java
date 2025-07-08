package com.moyorak.api.review.repository;

import com.moyorak.api.review.domain.ReviewPhoto;
import com.moyorak.api.review.dto.FirstReviewPhotoId;
import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {

    @Query(
            """
    SELECT new com.moyorak.api.review.dto.FirstReviewPhotoPath(r.teamRestaurantId, rp.path)
    FROM ReviewPhoto rp
    JOIN Review r ON rp.reviewId = r.id
    WHERE rp.id IN :reviewPhotoIds AND rp.use = true AND r.use = true
""")
    List<FirstReviewPhotoPath> findFirstReviewPhotoPathsByIdIn(
            @Param("reviewPhotoIds") List<Long> reviewPhotoIds);

    @Query(
            """
        SELECT new com.moyorak.api.review.dto.FirstReviewPhotoId(r.teamRestaurantId, MIN(rp.id))
        FROM ReviewPhoto rp
        JOIN Review r ON rp.reviewId = r.id
        WHERE r.teamRestaurantId IN :teamRestaurantIds AND r.use = true AND rp.use = true
        GROUP BY r.teamRestaurantId
""")
    List<FirstReviewPhotoId> findFirstReviewPhotoIdsByTeamRestaurantIds(
            @Param("teamRestaurantIds") List<Long> teamRestaurantIds);
}
