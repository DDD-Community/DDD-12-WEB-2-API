package com.moyorak.api.auth.dto;

import com.moyorak.api.auth.domain.MealTag;
import com.moyorak.api.auth.domain.MealTagType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Schema(title = "[마이] 알러지, 비선호 음식 상세 저장 요청 DTO")
public record MealTagDetailsSaveRequest(
        @NotNull @Schema(description = "구분 [DISLIKE | ALLERGY]", example = "DISLIKE")
                MealTagType type,
        @NotBlank @Schema(description = "항목", example = "우유") String item) {

    public MealTag toEntity(final Long userId) {
        return MealTag.create(userId, type, item);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MealTagDetailsSaveRequest that = (MealTagDetailsSaveRequest) o;
        return Objects.equals(item, that.item) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, item);
    }
}
