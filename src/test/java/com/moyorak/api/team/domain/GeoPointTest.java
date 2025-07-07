package com.moyorak.api.team.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GeoPointTest {

    @Test
    @DisplayName("하버사인 공식을 통해 두 지점 간 거리를 계산한다")
    void distanceTo() {
        // given
        // 기준점: 서울시청
        GeoPoint cityHall = GeoPoint.of(126.9779692, 37.566535);

        // 서울광장
        GeoPoint seoulPlaza = GeoPoint.of(126.977540, 37.565952);

        // 덕수궁
        GeoPoint deoksugung = GeoPoint.of(126.975184, 37.565804);

        // when
        double distanceToPlaza = cityHall.distanceTo(seoulPlaza);
        double distanceToDeoksugung = cityHall.distanceTo(deoksugung);

        // then
        assertThat(distanceToPlaza).isLessThan(distanceToDeoksugung);
    }
}
