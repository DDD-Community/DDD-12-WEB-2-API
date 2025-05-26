package com.moyorak.api.config.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "입력된 값에 대한 유효성 검사에 실패했을 경우의 응답값")
public class InvalidResponse {

    @Schema(description = "오류가 발생한 field명", example = "requestAmount")
    private final String field;

    @Schema(description = "오류 메세지", example = "입력되지 않은 값이 있습니다.")
    private final String message;

    @Schema(description = "잘못 입력된 field 입력 값", example = "ABCDE")
    private final Object rejectValue;
}
