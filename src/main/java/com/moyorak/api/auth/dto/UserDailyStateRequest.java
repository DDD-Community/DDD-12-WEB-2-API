package com.moyorak.api.auth.dto;

import com.moyorak.api.auth.domain.State;
import com.moyorak.api.auth.domain.UserDailyState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(title = "[마이] 혼밥 상태 업데이트 요청 DTO")
public record UserDailyStateRequest(
        @NotNull @Schema(description = "회원 고유 ID", example = "1") Long userId,
        @NotNull @Schema(description = "ON/OFF", example = "OFF") State state,
        @NotNull @Schema(description = "혼밥 상태 기준 날짜", example = "2025-06-23")
                LocalDate recordDate) {

    public UserDailyState toUserDailyState() {
        return UserDailyState.create(userId, state, recordDate);
    }
}
