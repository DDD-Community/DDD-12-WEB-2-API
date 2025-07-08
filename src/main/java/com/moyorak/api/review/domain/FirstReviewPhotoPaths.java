package com.moyorak.api.review.domain;

import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FirstReviewPhotoPaths {

    private final Map<Long, FirstReviewPhotoPath> photoPathMap;

    private FirstReviewPhotoPaths(final Map<Long, FirstReviewPhotoPath> photoPathMap) {
        this.photoPathMap = photoPathMap;
    }

    public static FirstReviewPhotoPaths create(
            final List<Long> teamRestaurantIds, final List<FirstReviewPhotoPath> photoPaths) {
        final Map<Long, FirstReviewPhotoPath> idToPhotoPath =
                photoPaths.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        FirstReviewPhotoPath::teamRestaurantId,
                                        Function.identity()));

        // 리뷰가 없는 팀 식당은 path가 null
        final Map<Long, FirstReviewPhotoPath> filledMap =
                teamRestaurantIds.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        Function.identity(),
                                        id ->
                                                idToPhotoPath.getOrDefault(
                                                        id, new FirstReviewPhotoPath(id, null))));

        return new FirstReviewPhotoPaths(filledMap);
    }

    public String getPhotoPath(final Long teamRestaurantId) {
        return photoPathMap.get(teamRestaurantId).path();
    }
}
