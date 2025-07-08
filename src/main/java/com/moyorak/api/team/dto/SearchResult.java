package com.moyorak.api.team.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public record SearchResult(List<Long> ids, Pageable pageable, Long total) {
    public Page<TeamRestaurantSearchResponse> toPage(
            final List<TeamRestaurantSearchResponse> teamRestaurantSearchResponses) {

        final Map<Long, TeamRestaurantSearchResponse> responseMap =
                teamRestaurantSearchResponses.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        TeamRestaurantSearchResponse::teamRestaurantId,
                                        Function.identity()));

        final List<TeamRestaurantSearchResponse> responsesInSearchOrder =
                ids.stream().map(responseMap::get).filter(Objects::nonNull).toList();

        return new PageImpl<>(responsesInSearchOrder, pageable, total);
    }
}
