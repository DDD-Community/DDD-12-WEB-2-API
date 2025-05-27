package com.moyorak.api.global.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListResponse<T> {

    @Schema(description = "한 페이지 아이템 갯수", example = "10")
    private int size;

    @Schema(description = "현재 페이지", example = "1")
    private int currentPage;

    @Schema(description = "총 리스트 갯수", example = "100")
    private long totalCount;

    @Schema(description = "리스트 값")
    private List<T> data;

    public static <T, R> ListResponse<R> from(Page<T> page, Function<T, R> converter) {
        List<R> data = page.getContent().stream().map(converter).collect(Collectors.toList());

        ListResponse<R> response = new ListResponse<>();

        response.size = page.getSize();
        response.currentPage = page.getNumber() + 1;
        response.data = data;
        response.totalCount = page.getTotalElements();

        return response;
    }
}
