package com.moyorak.api.restaurant.service;

import com.moyorak.api.restaurant.domain.Restaurant;
import com.moyorak.api.restaurant.domain.RestaurantSearch;
import com.moyorak.api.restaurant.dto.ExternalRestaurantSearchRequest;
import com.moyorak.api.restaurant.dto.ExternalRestaurantSearchResponse;
import com.moyorak.api.restaurant.dto.RestaurantSaveRequest;
import com.moyorak.api.restaurant.repository.RestaurantRepository;
import com.moyorak.api.restaurant.repository.RestaurantSearchRepository;
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
    private final RestaurantSearchRepository restaurantSearchRepository;

    public ListResponse<ExternalRestaurantSearchResponse> searchRestaurants(
            ExternalRestaurantSearchRequest searchRequest) {
        Page<KakaoPlace> page = kakaoSearcher.search(searchRequest.toKakaoSearchRequest());
        return ListResponse.from(page, ExternalRestaurantSearchResponse::fromKakaoPlace);
    }

    @Transactional
    public void save(final RestaurantSaveRequest restaurantSaveRequest) {

        final Restaurant restaurant = restaurantSaveRequest.toRestaurant();

        final boolean isSaved =
                restaurantRepository
                        .findByNameAndRoundedLongitudeAndRoundedLatitudeAndUseTrue(
                                restaurantSaveRequest.name(),
                                restaurant.getRoundedLongitude(),
                                restaurant.getRoundedLatitude())
                        .isPresent();

        if (isSaved) {
            throw new BusinessException("식당이 이미 등록되어 있습니다.");
        }

        final Restaurant saved = restaurantRepository.save(restaurant);
        restaurantSearchRepository.save(
                RestaurantSearch.create(saved.getId(), saved.getName(), saved.getRoadAddress()));
    }
}
