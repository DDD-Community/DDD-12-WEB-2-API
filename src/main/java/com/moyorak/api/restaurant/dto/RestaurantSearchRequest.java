package com.moyorak.api.restaurant.dto;

import com.moyorak.infra.client.kakao.dto.KakaoSearchRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springdoc.core.annotations.ParameterObject;

@ParameterObject
@Schema(title = "음식점 조회 요청 DTO")
public record RestaurantSearchRequest(
        @NotBlank(message = "키워드를 입력해주세요.") @Parameter(description = "키워드", example = "맥도날드")
                String query,
        @DecimalMin(value = "-180.0", message = "경도는 {value} 이상이어야 합니다.")
                @DecimalMax(value = "180.0", message = "경도는 {value} 이하여야 합니다.")
                @Parameter(description = "경도", example = "127.043616")
                Double x,
        @DecimalMin(value = "-90.0", message = "위도는 {value} 이상이어야 합니다.")
                @DecimalMax(value = "90.0", message = "위도는 {value} 이하여야 합니다.")
                @Parameter(description = "위도", example = "37.279838")
                Double y,
        @Min(value = 0, message = "반경은 {value}m 이상이어야 합니다.")
                @Max(value = 20000, message = "반경은 {value}m 이하여야 합니다.")
                @Parameter(description = "조회 범위", example = "2000")
                Integer radius,
        @Min(value = 1, message = "페이지 번호는 {value} 이상이어야 합니다.")
                @Max(value = 45, message = "페이지 번호는 {value} 이하여야 합니다.")
                @Parameter(description = "페이지 번호", example = "1")
                Integer page,
        @Min(value = 1, message = "한 페이지 결과 수는 {value} 이상이어야 합니다.")
                @Max(value = 15, message = "한 페이지 결과 수는 {value} 이하여야 합니다.")
                @Schema(description = "한 페이지 결과 수", example = "15")
                Integer size) {

    private static final String FOOD_CODE = "FD6";

    public KakaoSearchRequest toKakaoSearchRequest() {
        return new KakaoSearchRequest(query, x, y, radius, page, size, FOOD_CODE);
    }
}
