package com.moyorak.api.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "[이미지] 이미지 조회 응답 DTO")
public record ImagePresignedUrlResponse(
        @Schema(description = "이미지 Presigned URL", example = "https://...") String url) {

    public static ImagePresignedUrlResponse from(final String url) {
        return new ImagePresignedUrlResponse(url);
    }
}
