package com.moyorak.api.restaurant.service;

import com.moyorak.api.restaurant.dto.ExternalRestaurantSearchRequest;
import com.moyorak.api.restaurant.dto.ExternalRestaurantSearchResponse;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.api.restaurant.dto.RestaurantSearchResponse;
import com.moyorak.api.restaurant.repository.RestaurantSearchRepository;
import com.moyorak.global.domain.ListResponse;
import com.moyorak.infra.client.kakao.KakaoSearcher;
import com.moyorak.infra.client.kakao.dto.KakaoPlace;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final RestaurantSearchRepository restaurantSearchRepository;
    private final KakaoSearcher kakaoSearcher;

    public ListResponse<ExternalRestaurantSearchResponse> searchFromExternal(
            final ExternalRestaurantSearchRequest searchRequest) {
        final Page<KakaoPlace> page = kakaoSearcher.search(searchRequest.toKakaoSearchRequest());
        return ListResponse.from(page, ExternalRestaurantSearchResponse::fromKakaoPlace);
    }

    @Transactional(readOnly = true)
    public ListResponse<RestaurantSearchResponse> search(
            final RestaurantSearchRequest restaurantSearchRequest) {
        final Page<RestaurantSearchResponse> restaurantSearchResponses =
                RestaurantSearchResponse.from(
                        restaurantSearchRepository.searchByKeyword(
                                restaurantSearchRequest.getKeyword(),
                                restaurantSearchRequest.toPageable()));
        return ListResponse.from(restaurantSearchResponses);
    }
}
