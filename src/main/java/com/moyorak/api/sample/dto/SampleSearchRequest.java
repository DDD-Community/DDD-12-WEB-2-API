package com.moyorak.api.sample.dto;

import com.moyorak.api.global.domain.ListRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@ParameterObject
@RequiredArgsConstructor
@Schema(title = "[샘플] 샘플 검색 요청 DTO")
public class SampleSearchRequest extends ListRequest {

    @Size(max = 64, message = "제목은 최대 {max}자까지 입력 가능합니다.")
    @Parameter(description = "제목", example = "제목입니다.")
    private final String title;

    @Size(max = 200, message = "내용은 최대 {max}자까지 입력 가능합니다.")
    @Parameter(description = "내용", example = "내용입니다.")
    private final String content;
}
