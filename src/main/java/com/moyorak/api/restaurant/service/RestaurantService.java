package com.moyorak.api.restaurant.service;

import com.moyorak.api.restaurant.dto.RestaurantResponse;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.global.domain.ListResponse;
import com.moyorak.infra.client.kakao.KakaoSearcher;
import com.moyorak.infra.client.kakao.dto.KakaoPlace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final KakaoSearcher kakaoSearcher;

    public ListResponse<RestaurantResponse> searchRestaurants(
            RestaurantSearchRequest searchRequest) {
        Page<KakaoPlace> page = kakaoSearcher.search(searchRequest.toKakaoSearchRequest());
        return ListResponse.from(page, RestaurantResponse::fromKakaoPlace);
    }
}
