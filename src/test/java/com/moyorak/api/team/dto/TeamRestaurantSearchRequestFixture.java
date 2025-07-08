package com.moyorak.api.team.dto;

import com.moyorak.api.team.domain.SortOption;

public class TeamRestaurantSearchRequestFixture {

    public static TeamRestaurantSearchRequest fixture(
            String keyword, SortOption sortOption, Integer size, Integer currentPage) {
        return new TeamRestaurantSearchRequest(keyword, sortOption, size, currentPage);
    }
}
