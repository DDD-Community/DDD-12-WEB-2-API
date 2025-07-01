package com.moyorak.api.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImageDeleteRequest(
        @NotNull @Schema(description = "회원 고유 ID", example = "13") Long userId,
        @NotBlank
                @Schema(
                        description = "파일 저장 path",
                        example = "dev/20250630/ca811057-a488-42bf-96b4-290c2fc5a8ef-20_41_27.png")
                String path) {}
