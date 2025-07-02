package com.moyorak.api.team.domain;

public record TeamPlaceDistance(GeoPoint company, GeoPoint restaurant) {

    public double calculateDistance() {
        return company.distanceTo(restaurant);
    }

    public static TeamPlaceDistance of(GeoPoint company, GeoPoint restaurant) {
        return new TeamPlaceDistance(company, restaurant);
    }
}
