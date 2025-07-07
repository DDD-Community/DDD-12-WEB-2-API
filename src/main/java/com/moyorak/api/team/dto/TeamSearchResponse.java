package com.moyorak.api.team.dto;

import com.moyorak.api.team.domain.TeamSearch;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Schema(title = "팀 이름 검색 응답 DTO")
public record TeamSearchResponse(
        @Schema(description = "팀 고유 ID", example = "21") Long teamId,
        @Schema(description = "팀 이름", example = "Backend파트") String name) {

    public static TeamSearchResponse from(final TeamSearch team) {
        return new TeamSearchResponse(team.getId(), team.getName());
    }
}
