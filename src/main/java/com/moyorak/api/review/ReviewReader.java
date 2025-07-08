package com.moyorak.api.review;

import com.moyorak.api.review.dto.FirstReviewPhotoId;
import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import com.moyorak.api.review.repository.ReviewPhotoRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewReader {

    private final ReviewPhotoRepository reviewPhotoRepository;

    @Transactional(readOnly = true)
    public List<FirstReviewPhotoPath> findFirstReviewSummaries(final List<Long> teamRestaurantIds) {

        // 팀식당ID -> 포토 ID
        final List<FirstReviewPhotoId> firstReviewPhotoIds =
                reviewPhotoRepository.findFirstReviewPhotoIdsByTeamRestaurantIds(teamRestaurantIds);
        final List<Long> reviewPhotoIds =
                firstReviewPhotoIds.stream().map(FirstReviewPhotoId::reviewPhotoId).toList();

        // 포토 ID -> 포토 Path
        final List<FirstReviewPhotoPath> firstReviewPhotoPaths =
                reviewPhotoRepository.findFirstReviewPhotoPathsByIdIn(reviewPhotoIds);
        final Map<Long, FirstReviewPhotoPath> teamRestaurantIdToPath =
                firstReviewPhotoPaths.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        FirstReviewPhotoPath::teamRestaurantId,
                                        Function.identity()));

        return teamRestaurantIds.stream()
                .map(
                        teamRestaurantId ->
                                teamRestaurantIdToPath.getOrDefault(
                                        teamRestaurantId,
                                        new FirstReviewPhotoPath(teamRestaurantId, null)))
                .toList();
    }
}
