package com.moyorak.api.team.domain;

public record TeamRestaurantDistance(GeoPoint company, GeoPoint restaurant) {

    public double calculateDistance() {
        return company.distanceTo(restaurant);
    }

    public static TeamRestaurantDistance of(GeoPoint company, GeoPoint restaurant) {
        return new TeamRestaurantDistance(company, restaurant);
    }
}
