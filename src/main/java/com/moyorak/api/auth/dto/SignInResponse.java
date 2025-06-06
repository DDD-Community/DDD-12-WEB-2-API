package com.moyorak.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[로그인] 로그인 토큰 응답 DTO")
public record SignInResponse(@Schema(description = "액세스 토큰") String accessToken) {

    public static SignInResponse create(final String accessToken) {
        return new SignInResponse(accessToken);
    }
}
