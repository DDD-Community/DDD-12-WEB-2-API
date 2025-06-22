package com.moyorak.api.restaurant.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @DisplayName("음식점 등록")
    class Save {

        private RestaurantSaveRequest request;

        @BeforeEach
        void setUp() {
            request =
                    new RestaurantSaveRequest(
                            "123456",
                            "http://place.map.kakao.com/123456",
                            "우가우가 차차차",
                            "우가우가시 차차차동 24번길",
                            RestaurantCategory.KOREAN,
                            127.043616,
                            37.503095);
        }

        @Test
        @DisplayName("음식점 등록 성공")
        void success() {
            // given
            given(restaurantRepository.findByKakaoPlaceIdAndUseTrue(request.kakaoPlaceId()))
                    .willReturn(Optional.empty());

            // when
            restaurantService.save(request);

            // then
            then(restaurantRepository).should().save(any(Restaurant.class));
        }

        @Test
        @DisplayName("이미 등록된 카카오 ID가 있습니다.")
        void fail() {
            // given
            final Restaurant alreadyExist =
                    Restaurant.create(
                            request.kakaoPlaceId(),
                            request.kakaoPlaceUrl(),
                            request.name(),
                            request.address(),
                            request.category(),
                            request.longitude(),
                            request.latitude());

            given(restaurantRepository.findByKakaoPlaceIdAndUseTrue(request.kakaoPlaceId()))
                    .willReturn(Optional.of(alreadyExist));

            assertThrows(BusinessException.class, () -> restaurantService.save(request));
        }
    }
}
