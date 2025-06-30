package com.moyorak.api.restaurant.dto;

public class RestaurantSearchProjectionFixture {

    public static RestaurantSearchProjection fixture(
            final Long id, final String name, final String roadAddress) {
        return new RestaurantSearchProjection() {
            @Override
            public Long getRestaurantId() {
                return id;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getRoadAddress() {
                return roadAddress;
            }
        };
    }
}
