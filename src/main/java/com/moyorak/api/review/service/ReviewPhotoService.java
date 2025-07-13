package com.moyorak.api.review.service;

import com.moyorak.api.review.domain.FirstReviewPhotoPaths;
import com.moyorak.api.review.domain.ReviewPhotoPaths;
import com.moyorak.api.review.dto.FirstReviewPhotoId;
import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import com.moyorak.api.review.dto.ReviewPhotoPath;
import com.moyorak.api.review.repository.ReviewPhotoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewPhotoService {

    private final ReviewPhotoRepository reviewPhotoRepository;

    @Transactional(readOnly = true)
    public FirstReviewPhotoPaths findFirstReviewPhotoPaths(final List<Long> teamRestaurantIds) {
        // 팀식당ID -> 포토 ID
        final List<FirstReviewPhotoId> firstReviewPhotoIds =
                reviewPhotoRepository.findFirstReviewPhotoIdsByTeamRestaurantIds(teamRestaurantIds);
        final List<Long> reviewPhotoIds =
                firstReviewPhotoIds.stream().map(FirstReviewPhotoId::reviewPhotoId).toList();

        // 포토 ID -> 포토 Path
        final List<FirstReviewPhotoPath> firstReviewPhotoPaths =
                reviewPhotoRepository.findFirstReviewPhotoPathsByIdIn(reviewPhotoIds);

        return FirstReviewPhotoPaths.create(teamRestaurantIds, firstReviewPhotoPaths);
    }

    @Transactional(readOnly = true)
    public ReviewPhotoPaths getReviewPhotoPathsGroupedByReviewId(final List<Long> reviewIds) {
        final List<ReviewPhotoPath> reviewPhotos =
                reviewPhotoRepository.findPhotoPathsByReviewIds(reviewIds);

        return ReviewPhotoPaths.create(reviewPhotos);
    }

    @Transactional(readOnly = true)
    public List<String> getAllReviewPhotoPathsByTeamRestaurantId(Long teamRestaurantId) {
        return reviewPhotoRepository.findPhotoPathsByTeamRestaurantId(teamRestaurantId);
    }
}
