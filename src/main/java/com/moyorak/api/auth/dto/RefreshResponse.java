package com.moyorak.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[로그인] 로그인 토큰 응답 DTO")
public record RefreshResponse(
        @Schema(description = "액세스 토큰") String accessToken,
        @Schema(description = "리프레시 토큰") String refreshToken) {

    public static RefreshResponse create(final String accessToken, final String refreshToken) {
        return new RefreshResponse(accessToken, refreshToken);
    }
}
