package com.moyorak.api.team.service;

import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import com.moyorak.api.review.service.ReviewPhotoService;
import com.moyorak.api.team.dto.SearchResult;
import com.moyorak.api.team.dto.TeamRestaurantSearchRequest;
import com.moyorak.api.team.dto.TeamRestaurantSearchResponse;
import com.moyorak.api.team.dto.TeamRestaurantSearchSummary;
import com.moyorak.global.domain.ListResponse;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRestaurantSearchFacade {

    private final TeamRestaurantService teamRestaurantService;
    private final TeamRestaurantSearchService teamRestaurantSearchService;
    private final ReviewPhotoService reviewPhotoService;

    @Transactional(readOnly = true)
    public ListResponse<TeamRestaurantSearchResponse> search(
            final Long teamId, final TeamRestaurantSearchRequest teamRestaurantSearchRequest) {

        // 서치 서비스에서 id들 가져오기
        final SearchResult searchResult =
                teamRestaurantSearchService.search(
                        teamId,
                        teamRestaurantSearchRequest.getKeyword(),
                        teamRestaurantSearchRequest.toPageable());

        // 팀 식당 id로 필요한 정보 가져오기
        final List<TeamRestaurantSearchSummary> teamRestaurantSearchSummaries =
                teamRestaurantService.findByIdsAndUse(searchResult.ids(), true);
        final List<Long> teamRestaurantIds =
                teamRestaurantSearchSummaries.stream()
                        .map(TeamRestaurantSearchSummary::teamRestaurantId)
                        .toList();

        // 팀 식당별로 리뷰 첫 사진 정보 가져오기
        final List<FirstReviewPhotoPath> firstReviewPhotoPaths =
                reviewPhotoService.findFirstReviewPhotoPaths(teamRestaurantIds);
        final Map<Long, FirstReviewPhotoPath> teamRestaurantIdToPhotoPaths =
                firstReviewPhotoPaths.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        FirstReviewPhotoPath::teamRestaurantId,
                                        Function.identity()));

        final List<TeamRestaurantSearchResponse> teamRestaurantSearchResponses =
                teamRestaurantSearchSummaries.stream()
                        .map(
                                teamRestaurantSearchSummary ->
                                        TeamRestaurantSearchResponse.from(
                                                teamRestaurantSearchSummary,
                                                teamRestaurantIdToPhotoPaths.get(
                                                        teamRestaurantSearchSummary
                                                                .teamRestaurantId())))
                        .toList();

        return ListResponse.from(searchResult.toPage(teamRestaurantSearchResponses));
    }
}
