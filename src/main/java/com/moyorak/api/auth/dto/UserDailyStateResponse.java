package com.moyorak.api.auth.dto;

import com.moyorak.api.auth.domain.State;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(title = "[마이] 혼밥 상태 응답 DTO")
public record UserDailyStateResponse(
        @NotNull @Schema(description = "ON/OFF", example = "OFF") State state) {
    public static UserDailyStateResponse from(State state) {
        return new UserDailyStateResponse(state);
    }
}
