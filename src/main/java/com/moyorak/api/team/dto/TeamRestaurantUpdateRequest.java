package com.moyorak.api.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Schema(title = "팀 맛집 정보 수정 DTO")
public record TeamRestaurantUpdateRequest(
        @Schema(description = "한줄 소개", example = "여기 진짜 맛있습니다.") String summary) {}
