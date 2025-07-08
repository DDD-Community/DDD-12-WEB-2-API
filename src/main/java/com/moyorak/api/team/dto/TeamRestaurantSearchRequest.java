package com.moyorak.api.team.dto;

import com.moyorak.api.team.domain.SortOption;
import com.moyorak.global.domain.ListRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Schema(title = "[팀 맛집] 팀 맛집 검색 요청 DTO")
public class TeamRestaurantSearchRequest extends ListRequest {
    @NotBlank(message = "키워드를 입력해주세요.")
    @Size(min = 2, message = "키워드는 {min}자 이상이어야 합니다.")
    @Schema(description = "검색 키워드", example = "김밥")
    private final String keyword;

    @NotNull(message = "정렬 조건을 입력해주세요.")
    @Schema(description = "정렬 조건", example = "DISTANCE")
    private final SortOption sortOption;

    protected TeamRestaurantSearchRequest(
            String keyword, SortOption sortOption, Integer size, Integer currentPage) {
        super(size, currentPage);
        this.keyword = keyword;
        this.sortOption = sortOption;
    }

    @Override
    public Pageable toPageable() {
        return PageRequest.of(super.getCurrentPage() - 1, super.getSize(), sortOption.toSort());
    }
}
