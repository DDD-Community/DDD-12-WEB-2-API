package com.moyorak.api.team.domain;

public record GeoPoint(double longitude, double latitude) {

    /**
     * 대상 지점(target)과의 거리(미터 단위)를 하버사인(Haversine) 공식을 사용해 계산한다.
     *
     * @param target 거리 계산 대상 지점
     * @return 두 지점 간 거리 (미터 단위)
     */
    public double distanceTo(GeoPoint target) {
        double R = 6371e3; // 지구 반지름 (m)
        double lat1Rad = Math.toRadians(this.latitude);
        double lat2Rad = Math.toRadians(target.latitude);
        double deltaLat = Math.toRadians(target.latitude - this.latitude);
        double deltaLon = Math.toRadians(target.longitude - this.longitude);

        double a =
                Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                        + Math.cos(lat1Rad)
                                * Math.cos(lat2Rad)
                                * Math.sin(deltaLon / 2)
                                * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // 미터 단위 거리
    }

    public static GeoPoint of(double longitude, double latitude) {
        return new GeoPoint(longitude, latitude);
    }
}
