package com.moyorak.api.restaurant.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.restaurant.domain.RestaurantCategory;
import com.moyorak.api.restaurant.dto.RestaurantSaveRequest;
import com.moyorak.api.restaurant.repository.RestaurantRepository;
import com.moyorak.config.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @InjectMocks private RestaurantService restaurantService;

    @Mock private RestaurantRepository restaurantRepository;

    @Nested
    @DisplayName("음식점 등록 시")
    class Save {

        private RestaurantSaveRequest request;

        @BeforeEach
        void setUp() {
            request =
                    new RestaurantSaveRequest(
                            "http://place.map.kakao.com/123456",
                            "우가우가 차차차",
                            "우가우가시 차차차동 24번길",
                            RestaurantCategory.KOREAN,
                            127.043616,
                            37.503095);
        }

        @Test
        @DisplayName("저장을 성공합니다.")
        void success() {
            // given
            Restaurant restaurant = request.toRestaurant();
            given(
                            restaurantRepository
                                    .findByNameAndRoundedLongitudeAndRoundedLatitudeAndUseTrue(
                                            restaurant.getName(),
                                            restaurant.getRoundedLongitude(),
                                            restaurant.getRoundedLatitude()))
                    .willReturn(Optional.empty());

            // when
            restaurantService.save(request);

            // then
            then(restaurantRepository).should().save(any(Restaurant.class));
        }

        @Test
        @DisplayName("이미 등록된 식당이 있습니다.")
        void fail() {
            // given
            final Restaurant newRestaurant = request.toRestaurant();
            final Restaurant existingRestaurant =
                    Restaurant.create(
                            "http://place.map.kakao.com/123456",
                            "우가우가 차차차",
                            "우가우가시 차차차동 24번길",
                            RestaurantCategory.KOREAN,
                            127.043616,
                            37.503095);

            given(
                            restaurantRepository
                                    .findByNameAndRoundedLongitudeAndRoundedLatitudeAndUseTrue(
                                            newRestaurant.getName(),
                                            newRestaurant.getRoundedLongitude(),
                                            newRestaurant.getRoundedLatitude()))
                    .willReturn(Optional.of(existingRestaurant));

            // when & then
            assertThatThrownBy(() -> restaurantService.save(request))
                    .isInstanceOf(BusinessException.class);
        }
    }
}
