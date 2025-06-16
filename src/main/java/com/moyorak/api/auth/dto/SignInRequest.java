package com.moyorak.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(title = "[로그인] 로그인 요청 DTO")
public record SignInRequest(
        @NotNull @Schema(description = "회원 고유 ID", example = "13") Long userId) {}
