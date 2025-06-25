package com.moyorak.api.restaurant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestaurantTest {

    @Test
    @DisplayName("Restaurant 생성 시 위도, 경도가 5자리로 반올림되어 저장된다")
    void create() {
        // given
        final double longitude = 127.9876543;
        final double latitude = 37.1234567;

        // when
        Restaurant restaurant =
                Restaurant.create(
                        "http://place.map.kakao.com/123456",
                        "우가우가 차차차",
                        "서울시 차차차동 24번길",
                        "우가우가 차차로 123",
                        RestaurantCategory.KOREAN,
                        longitude,
                        latitude);

        // then
        assertThat(restaurant.getRoundedLongitude()).isEqualTo(new BigDecimal("127.98765"));
        assertThat(restaurant.getRoundedLatitude()).isEqualTo(new BigDecimal("37.12346"));
    }
}
