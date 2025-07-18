package com.moyorak.api.team.dto;

public record TeamRestaurantSearchEvent(Long userId, Long teamId, String keyword) {
    public static TeamRestaurantSearchEvent of(Long userId, Long teamId, String keyword) {
        return new TeamRestaurantSearchEvent(userId, teamId, keyword);
    }
}
