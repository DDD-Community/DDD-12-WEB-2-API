package com.moyorak.api.restaurant.service;

import com.moyorak.api.restaurant.dto.RestaurantResponse;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.api.restaurant.external.KakaoSearcher;
import com.moyorak.api.restaurant.external.dto.KakaoPlace;
import com.moyorak.api.restaurant.external.dto.KakoSearchResponse;
import com.moyorak.global.domain.ListResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {

    private final KakaoSearcher kakaoSearcher;

    public ListResponse<RestaurantResponse> searchRestaurant(
            RestaurantSearchRequest searchRequest) {
        KakoSearchResponse kakoSearchResponse =
                kakaoSearcher.search(searchRequest.toKakaoSearchRequest());
        Page<KakaoPlace> page =
                createPage(kakoSearchResponse, searchRequest.page(), searchRequest.size());
        return ListResponse.from(page, RestaurantResponse::fromKakaoPlace);
    }

    private Page<KakaoPlace> createPage(
            KakoSearchResponse kakoSearchResponse, int currentPage, int size) {
        int offset = (currentPage - 1) * size;
        PageRequest pageRequest = PageRequest.of(currentPage - 1, size);
        if (offset >= kakoSearchResponse.meta().pageable_count()) {
            return new PageImpl<>(
                    Collections.emptyList(),
                    pageRequest,
                    kakoSearchResponse.meta().pageable_count());
        }
        return new PageImpl<>(
                kakoSearchResponse.documents(),
                pageRequest,
                kakoSearchResponse.meta().pageable_count());
    }
}
