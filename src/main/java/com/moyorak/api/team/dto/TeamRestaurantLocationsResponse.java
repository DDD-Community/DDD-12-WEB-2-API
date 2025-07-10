package com.moyorak.api.team.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(title = "[팀 맛집] 팀 맛집 경도, 위도 조회 응답 리스트 DTO")
public record TeamRestaurantLocationsResponse(
        @ArraySchema(
                        schema =
                                @Schema(
                                        description = "팀 맛집 위치 정보",
                                        implementation = TeamRestaurantLocation.class),
                        arraySchema = @Schema(description = "팀 맛집 경도, 위도 리스트"))
                List<TeamRestaurantLocation> locations) {
    public static TeamRestaurantLocationsResponse of(final List<TeamRestaurantLocation> locations) {
        return new TeamRestaurantLocationsResponse(locations);
    }
}
