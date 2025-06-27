package com.moyorak.api.restaurant.controller;

import com.moyorak.api.restaurant.dto.ExternalRestaurantSearchRequest;
import com.moyorak.api.restaurant.dto.ExternalRestaurantSearchResponse;
import com.moyorak.api.restaurant.dto.RestaurantSaveRequest;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.api.restaurant.dto.RestaurantSearchResponse;
import com.moyorak.api.restaurant.service.RestaurantSearchService;
import com.moyorak.api.restaurant.service.RestaurantService;
import com.moyorak.global.domain.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@SecurityRequirement(name = "JWT")
@Tag(name = "음식점 API", description = "음식점 API 입니다.")
class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantSearchService restaurantSearchService;

    @GetMapping
    @Operation(summary = "음식점 데이터 조회", description = "음식점 데이터 리스트를 검색합니다.")
    public ListResponse<ExternalRestaurantSearchResponse> searchRestaurants(
            @Valid ExternalRestaurantSearchRequest searchRequest) {
        return restaurantService.searchRestaurants(searchRequest);
    }

    @PostMapping
    @Operation(summary = "음식점 데이터 저장", description = "음식점 데이터를 저장합니다.")
    public void save(@Valid @RequestBody final RestaurantSaveRequest restaurantSaveRequest) {
        restaurantService.save(restaurantSaveRequest);
    }

    @GetMapping("/search")
    @Operation(summary = "음식점 데이터 검색 (모여락)", description = "음식점 데이터 리스트를 검색합니다.")
    public ListResponse<RestaurantSearchResponse> searchRestaurants(
            @Valid RestaurantSearchRequest searchRequest) {
        return restaurantSearchService.search(searchRequest);
    }
}
