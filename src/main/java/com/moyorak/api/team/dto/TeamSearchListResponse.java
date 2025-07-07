package com.moyorak.api.team.dto;

import com.moyorak.api.team.domain.TeamSearch;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Schema(title = "팀 이름 검색 응답 DTO")
public record TeamSearchListResponse(
        @ArraySchema(
                        schema =
                                @Schema(
                                        description = "팀 정보",
                                        implementation = TeamSearchResponse.class),
                        arraySchema = @Schema(description = "팀 정보 검색 응답 리스트"))
                List<TeamSearchResponse> teams) {
    public static TeamSearchListResponse from(final List<TeamSearch> entities) {
        return new TeamSearchListResponse(
                entities.stream().map(TeamSearchResponse::from).collect(Collectors.toList()));
    }
}
