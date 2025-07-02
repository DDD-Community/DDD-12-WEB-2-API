package com.moyorak.api.team.dto;

import com.moyorak.api.team.domain.TeamPlace;

public record TeamPlaceSaveRequest(String summary) {
    public TeamPlace toTeamPlace(Long teamId, Long restaurantId, double distanceFromTeam) {
        return TeamPlace.create(summary, teamId, restaurantId, distanceFromTeam);
    }
}
