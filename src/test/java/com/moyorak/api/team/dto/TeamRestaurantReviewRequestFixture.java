package com.moyorak.api.team.dto;

public class TeamRestaurantReviewRequestFixture {
    public static TeamRestaurantReviewRequest fixture(Integer currentPage, Integer size) {
        return new TeamRestaurantReviewRequest(currentPage, size);
    }
}
