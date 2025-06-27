package com.moyorak.api.restaurant.service;

import com.moyorak.api.restaurant.domain.RestaurantSearch;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.api.restaurant.dto.RestaurantSearchResponse;
import com.moyorak.api.restaurant.repository.RestaurantSearchRepository;
import com.moyorak.global.domain.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {

    private final RestaurantSearchRepository restaurantSearchRepository;

    @Transactional(readOnly = true)
    public ListResponse<RestaurantSearchResponse> search(
            final RestaurantSearchRequest restaurantSearchRequest) {
        final Page<RestaurantSearch> restaurantSearches =
                restaurantSearchRepository.searchByKeyword(
                        restaurantSearchRequest.getKeyword(), restaurantSearchRequest.toPageable());
        return ListResponse.from(restaurantSearches, RestaurantSearchResponse::from);
    }
}
