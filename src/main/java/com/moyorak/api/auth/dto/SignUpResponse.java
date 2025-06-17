package com.moyorak.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[회원가입] 간편 회원가입 완료 응답 DTO")
public record SignUpResponse(
        @Schema(description = "회원 고유 ID") Long id, @Schema(description = "회원 이름") String name) {}
