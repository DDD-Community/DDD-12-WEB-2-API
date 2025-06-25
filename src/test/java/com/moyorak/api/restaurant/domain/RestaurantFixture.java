package com.moyorak.api.restaurant.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class RestaurantFixture {

    public static Restaurant fixture(
            final String placeUrl,
            final String name,
            final String address,
            final String roadAddress,
            final RestaurantCategory category,
            final double longitude,
            final double latitude) {
        return Restaurant.create(
                placeUrl, name, address, roadAddress, category, longitude, latitude);
    }

    public static Restaurant fixtureWithId(
            final Long id,
            final String placeUrl,
            final String name,
            final String address,
            final String roadAddress,
            final RestaurantCategory category,
            final double longitude,
            final double latitude) {
        Restaurant restaurant =
                fixture(placeUrl, name, address, roadAddress, category, longitude, latitude);
        ReflectionTestUtils.setField(restaurant, "id", id);
        return restaurant;
    }
}
