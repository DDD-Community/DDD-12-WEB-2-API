package com.moyorak.api.restaurant.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class RestaurantFixture {

    public static Restaurant fixture(
            final Long id,
            final String name,
            final double longitude,
            final double latitude,
            final boolean use) {
        Restaurant restaurant = new Restaurant();

        ReflectionTestUtils.setField(restaurant, "id", id);
        ReflectionTestUtils.setField(restaurant, "name", name);
        ReflectionTestUtils.setField(restaurant, "longitude", longitude);
        ReflectionTestUtils.setField(restaurant, "latitude", latitude);
        ReflectionTestUtils.setField(restaurant, "use", use);

        return restaurant;
    }

    public static Restaurant fixture(final String name, final RestaurantCategory category) {
        Restaurant restaurant = new Restaurant();
        ReflectionTestUtils.setField(restaurant, "name", name);
        ReflectionTestUtils.setField(restaurant, "category", category);
        return restaurant;
    }

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
