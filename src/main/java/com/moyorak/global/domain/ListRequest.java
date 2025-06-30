package com.moyorak.global.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
public abstract class ListRequest {

    private static final int DEFAULT_LIST_SIZE = 10;
    private static final int DEFAULT_PAGE = 1;

    @Schema(description = "한 페이지 아이템 갯수", example = "10")
    private final Integer size;

    @Positive
    @Schema(description = "조회 페이지", example = "1")
    private final Integer currentPage;

    protected ListRequest() {
        this.size = DEFAULT_LIST_SIZE;
        this.currentPage = DEFAULT_PAGE;
    }

    protected ListRequest(Integer size, Integer currentPage) {
        this.size = size;
        this.currentPage = currentPage;
    }

    public Pageable toPageableAndDateSorted() {
        final Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        return PageRequest.of(currentPage - 1, size, sort);
    }

    public Pageable toPageable() {
        return PageRequest.of(currentPage - 1, size);
    }
}
