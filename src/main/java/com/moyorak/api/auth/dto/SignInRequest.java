package com.moyorak.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(title = "[로그인] 로그인 요청 DTO")
public record SignInRequest(
        @NotNull @Schema(description = "회원 고유 ID", example = "13") Long userId,
        @NotBlank @Schema(description = "이메일", example = "honggildong@gmail.com") String email,
        @NotBlank @Schema(description = "회원 이름", example = "홍길동") String name) {}
