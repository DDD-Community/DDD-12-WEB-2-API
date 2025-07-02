package com.moyorak.api.team.dto;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.team.domain.TeamRestaurant;

public record TeamPlaceSaveRequest(String summary) {
    public TeamRestaurant toTeamRestaurant(
            Long teamId, Restaurant restaurant, double distanceFromTeam) {
        return TeamRestaurant.create(teamId, restaurant, summary, distanceFromTeam);
    }
}
