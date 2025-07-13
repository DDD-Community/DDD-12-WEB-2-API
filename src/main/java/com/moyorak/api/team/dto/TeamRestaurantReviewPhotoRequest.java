package com.moyorak.api.team.dto;

import com.moyorak.global.domain.ListRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@ParameterObject
@Schema(title = "리뷰 사진 조회 요청 DTO")
public class TeamRestaurantReviewPhotoRequest extends ListRequest {

    public TeamRestaurantReviewPhotoRequest(Integer currentPage, Integer size) {
        super(size, currentPage);
    }
}
