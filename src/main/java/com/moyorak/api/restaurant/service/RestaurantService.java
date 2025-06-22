package com.moyorak.api.restaurant.service;

import com.moyorak.api.restaurant.dto.RestaurantResponse;
import com.moyorak.api.restaurant.dto.RestaurantSaveRequest;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.api.restaurant.repository.RestaurantRepository;
import com.moyorak.config.exception.BusinessException;
import com.moyorak.global.domain.ListResponse;
import com.moyorak.infra.client.kakao.KakaoSearcher;
import com.moyorak.infra.client.kakao.dto.KakaoPlace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final KakaoSearcher kakaoSearcher;
    private final RestaurantRepository restaurantRepository;

    public ListResponse<RestaurantResponse> searchRestaurants(
            RestaurantSearchRequest searchRequest) {
        Page<KakaoPlace> page = kakaoSearcher.search(searchRequest.toKakaoSearchRequest());
        return ListResponse.from(page, RestaurantResponse::fromKakaoPlace);
    }

    // TODO 검색용 디비에 저장 추가
    @Transactional
    public void save(final RestaurantSaveRequest restaurantSaveRequest) {
        restaurantRepository
                .findByKakaoPlaceIdAndUseTrue(restaurantSaveRequest.kakaoPlaceId())
                .ifPresent(
                        restaurant -> {
                            throw new BusinessException("이미 등록된 식당입니다.");
                        });
        restaurantRepository.save(restaurantSaveRequest.toRestaurant());
    }
}
