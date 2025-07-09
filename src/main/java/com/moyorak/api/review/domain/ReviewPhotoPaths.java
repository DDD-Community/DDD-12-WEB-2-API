package com.moyorak.api.review.domain;

import com.moyorak.api.review.dto.ReviewPhotoPath;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReviewPhotoPaths {
    private final Map<Long, List<String>> reviewPhotoPathsMap;

    private ReviewPhotoPaths(final Map<Long, List<String>> reviewPhotoPathsMap) {
        this.reviewPhotoPathsMap = reviewPhotoPathsMap;
    }

    public static ReviewPhotoPaths create(final List<ReviewPhotoPath> reviewPhotoPaths) {
        final Map<Long, List<String>> reviewPhotoPathsMap =
                reviewPhotoPaths.stream()
                        .collect(
                                Collectors.groupingBy(
                                        ReviewPhotoPath::reviewId, // key: 리뷰 ID
                                        Collectors.mapping(
                                                ReviewPhotoPath::reviewPhotoPath,
                                                Collectors.toList()) // value: path list
                                        ));

        return new ReviewPhotoPaths(reviewPhotoPathsMap);
    }

    public List<String> getPhotoPaths(final Long reviewId) {
        return reviewPhotoPathsMap.getOrDefault(reviewId, List.of());
    }
}
