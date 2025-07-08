package com.moyorak.api.team.domain;

import com.moyorak.api.review.domain.FirstReviewPhotoPaths;
import com.moyorak.api.team.dto.TeamRestaurantSearchResponse;
import com.moyorak.api.team.dto.TeamRestaurantSearchSummary;
import java.util.List;

public class TeamRestaurantSearchSummaries {

    private final List<TeamRestaurantSearchSummary> summaries;

    private TeamRestaurantSearchSummaries(final List<TeamRestaurantSearchSummary> summary) {
        this.summaries = summary;
    }

    public static TeamRestaurantSearchSummaries create(
            final List<TeamRestaurantSearchSummary> summary) {
        return new TeamRestaurantSearchSummaries(summary);
    }

    public List<Long> getTeamRestaurantIds() {
        return summaries.stream().map(TeamRestaurantSearchSummary::teamRestaurantId).toList();
    }

    public List<TeamRestaurantSearchResponse> toResponse(FirstReviewPhotoPaths photoPaths) {
        return summaries.stream()
                .map(
                        teamRestaurantSearchSummary ->
                                TeamRestaurantSearchResponse.from(
                                        teamRestaurantSearchSummary,
                                        photoPaths.getPhotoPath(
                                                teamRestaurantSearchSummary.teamRestaurantId())))
                .toList();
    }
}
