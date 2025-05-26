package com.moyorak.api.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(title = "[샘플] 샘플 수정 요청 DTO")
public record SampleUpdateRequest(
        @NotBlank(message = "제목을 입력해주세요.")
                @Size(max = 64, message = "제목은 최대 {max}자까지 입력 가능합니다.")
                @Schema(description = "제목", example = "제목입니다.")
                String title,
        @NotBlank(message = "내용을 입력해주세요.")
                @Size(max = 200, message = "내용은 최대 {max}자까지 입력 가능합니다.")
                @Schema(description = "내용", example = "내용입니다.")
                String content) {}
