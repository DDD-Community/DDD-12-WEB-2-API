package com.moyorak.api.team.dto;

import com.moyorak.api.restaurant.domain.RestaurantCategory;

public class TeamRestaurantSearchSummaryFixture {

    public static TeamRestaurantSearchSummary fixture(
            Long teamRestaurantId,
            String restaurantName,
            RestaurantCategory restaurantCategory,
            double averageReviewScore,
            int reviewCount) {
        return new TeamRestaurantSearchSummary(
                teamRestaurantId,
                restaurantName,
                restaurantCategory,
                averageReviewScore,
                reviewCount);
    }
}
