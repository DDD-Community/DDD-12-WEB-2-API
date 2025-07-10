package com.moyorak.api.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[팀 맛집] 팀 맛집 경도, 위도 조회 응답 DTO")
public record TeamRestaurantLocation(
        @Schema(description = "팀 맛집 고유 ID", example = "3") Long teamRestaurantId,
        @Schema(description = "팀 맛집 식당 이름", example = "모여락 부대찌개") String name,
        @Schema(description = "식당 경도", example = "126.990774") double longitude,
        @Schema(description = "식당 위도", example = "37.566345") double latitude) {}
