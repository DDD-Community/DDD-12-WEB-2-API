package com.moyorak.api.auth.dto;

import com.moyorak.api.auth.domain.FoodFlag;
import com.moyorak.api.auth.domain.FoodFlagType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Schema(title = "[마이] 알러지, 비선호 음식 상세 저장 요청 DTO")
public record FoodFlagDetailsSaveRequest(
        @NotNull @Schema(description = "회원 고유 ID", example = "13") Long userId,
        @NotNull @Schema(description = "구분 [DISLIKE | ALLERGY]", example = "DISLIKE")
                FoodFlagType type,
        @NotBlank @Schema(description = "항목", example = "우유, 계란, 갑각류") String item) {

    public FoodFlag toEntity() {
        return FoodFlag.create(userId, type, item);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoodFlagDetailsSaveRequest that = (FoodFlagDetailsSaveRequest) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(item, that.item)
                && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, type, item);
    }
}
