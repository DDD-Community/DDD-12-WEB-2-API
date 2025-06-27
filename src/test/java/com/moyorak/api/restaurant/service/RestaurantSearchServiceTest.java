package com.moyorak.api.restaurant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.restaurant.domain.RestaurantSearch;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.api.restaurant.dto.RestaurantSearchResponse;
import com.moyorak.api.restaurant.repository.RestaurantSearchRepository;
import com.moyorak.global.domain.ListResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class RestaurantSearchServiceTest {

    @InjectMocks private RestaurantSearchService restaurantSearchService;

    @Mock private RestaurantSearchRepository restaurantSearchRepository;

    @Test
    @DisplayName("검색을 성공합니다.")
    void success() {
        // given
        final String keyword = "김밥";
        final int page = 1;
        final int size = 10;
        final RestaurantSearchRequest request = new RestaurantSearchRequest(keyword, page, size);

        final RestaurantSearch restaurantSearch = RestaurantSearch.create(1L, "김밥천국", "도로명 주소");

        Page<RestaurantSearch> restaurantSearchPage =
                new PageImpl<>(List.of(restaurantSearch), PageRequest.of(page, size), 1);

        given(
                        restaurantSearchRepository.searchByKeyword(
                                request.getKeyword(), request.toPageable()))
                .willReturn(restaurantSearchPage);

        // when
        final ListResponse<RestaurantSearchResponse> result =
                restaurantSearchService.search(request);

        // then
        assertThat(result.getData().get(0).restaurantName()).contains(keyword);
    }
}
