package com.moyorak.api.review.dto;

import com.moyorak.global.domain.ListRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@ParameterObject
@Schema(title = "리뷰 조회 요청 DTO")
public class ReviewSearchRequest extends ListRequest {

    public ReviewSearchRequest(Integer currentPage, Integer size) {
        super(size, currentPage);
    }
}
