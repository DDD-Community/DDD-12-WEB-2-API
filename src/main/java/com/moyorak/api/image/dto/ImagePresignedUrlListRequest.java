package com.moyorak.api.image.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(title = "[이미지] 이미지 조회 리스트 요청 DTO")
public record ImagePresignedUrlListRequest(
        @NotNull
                @Size(min = 1, max = 10)
                @ArraySchema(
                        schema =
                                @Schema(
                                        description = "파일 조회 path 리스트",
                                        implementation = ImagePresignedUrlRequest.class),
                        arraySchema = @Schema(description = "Presigned URL 요청 리스트"))
                List<ImagePresignedUrlRequest> paths) {}
