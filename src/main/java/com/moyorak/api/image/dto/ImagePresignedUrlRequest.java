package com.moyorak.api.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(title = "[이미지] 이미지 조회 요청 DTO")
public record ImagePresignedUrlRequest(
        @NotBlank
                @Schema(
                        description = "파일 조회 path",
                        example = "dev/20250630/ca811057-a488-42bf-96b4-290c2fc5a8ef-20_41_27.png")
                String path) {}
