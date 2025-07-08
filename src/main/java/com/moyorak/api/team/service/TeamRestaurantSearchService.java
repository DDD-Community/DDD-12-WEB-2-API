package com.moyorak.api.team.service;

import com.moyorak.api.review.ReviewReader;
import com.moyorak.api.review.dto.FirstReviewPhotoPath;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.dto.SearchResult;
import com.moyorak.api.team.dto.TeamRestaurantSearchRequest;
import com.moyorak.api.team.dto.TeamRestaurantSearchResponse;
import com.moyorak.api.team.repository.TeamRestaurantRepository;
import com.moyorak.api.team.repository.TeamRestaurantSearchNativeRepository;
import com.moyorak.global.domain.ListResponse;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRestaurantSearchService {

    private final TeamRestaurantSearchNativeRepository nativeRepository;
    private final TeamRestaurantRepository teamRestaurantRepository;
    private final ReviewReader reviewReader;

    @Transactional(readOnly = true)
    public ListResponse<TeamRestaurantSearchResponse> search(
            final Long teamId,
            final TeamRestaurantSearchRequest teamRestaurantSearchRequest,
            Pageable pageable) {
        // 서치 레포지토리에서 id들 가져오기
        final SearchResult searchResult =
                nativeRepository.searchByTeamIdAndName(
                        teamId, teamRestaurantSearchRequest.getKeyword(), pageable);

        // 팀 식당 id로 필요한 정보 가져오기
        final List<TeamRestaurant> teamRestaurants =
                teamRestaurantRepository.findByIdInAndUse(searchResult.ids(), true);
        final List<Long> teamRestaurantIds =
                teamRestaurants.stream().map(TeamRestaurant::getId).toList();

        // 팀 식당별로 리뷰 첫 사진 정보 가져오기
        final List<FirstReviewPhotoPath> firstReviewPhotoPaths =
                reviewReader.findFirstReviewSummaries(teamRestaurantIds);
        final Map<Long, FirstReviewPhotoPath> teamRestaurantIdToPhotoPaths =
                firstReviewPhotoPaths.stream()
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        FirstReviewPhotoPath::teamRestaurantId,
                                        Function.identity()));

        final List<TeamRestaurantSearchResponse> teamRestaurantSearchResponses =
                teamRestaurants.stream()
                        .map(
                                teamRestaurant ->
                                        TeamRestaurantSearchResponse.from(
                                                teamRestaurant,
                                                teamRestaurantIdToPhotoPaths.get(
                                                        teamRestaurant.getId())))
                        .toList();

        return ListResponse.from(searchResult.toPage(teamRestaurantSearchResponses));
    }
}
