package com.moyorak.api.image.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(title = "[이미지] 이미지 조회 리스트 응답 DTO")
public record ImagePresignedUrlListResponse(
        @ArraySchema(
                        schema =
                                @Schema(
                                        description = "이미지 url 리스트",
                                        implementation = ImagePresignedUrlResponse.class),
                        arraySchema = @Schema(description = "Presigned URL 응답 리스트"))
                List<ImagePresignedUrlResponse> urls) {

    public static ImagePresignedUrlListResponse from(final List<ImagePresignedUrlResponse> urls) {
        return new ImagePresignedUrlListResponse(urls);
    }
}
