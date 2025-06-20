package com.moyorak.api.restaurant.controller;

import com.moyorak.api.restaurant.dto.RestaurantResponse;
import com.moyorak.api.restaurant.dto.RestaurantSearchRequest;
import com.moyorak.api.restaurant.service.RestaurantService;
import com.moyorak.global.domain.ListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
@SecurityRequirement(name = "JWT")
@Tag(name = "음식점 API", description = "음식점 API 입니다.")
class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "음식점 데이터 조회", description = "음식점 데이터 리스트를 검색합니다.")
    public ListResponse<RestaurantResponse> searchRestaurants(
            @Valid RestaurantSearchRequest searchRequest) {
        return restaurantService.searchRestaurants(searchRequest);
    }
}
