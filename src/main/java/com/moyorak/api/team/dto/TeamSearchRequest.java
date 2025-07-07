package com.moyorak.api.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Schema(title = "팀 이름 검색 요청 DTO")
public record TeamSearchRequest(
        @Schema(description = "팀 고유 ID", example = "21") Long teamId,
        @Schema(description = "팀 이름", example = "Backend") String name) {}
