package com.moyorak.api.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyorak.global.domain.ListRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springdoc.core.annotations.ParameterObject;

@Getter
@ParameterObject
@Schema(title = "음식점 검색 요청 DTO (모여락 DB)")
public class RestaurantSearchRequest extends ListRequest {

    @NotBlank(message = "키워드를 입력해주세요.")
    @Parameter(description = "키워드", example = "맥도날드")
    private final String keyword;

    @JsonCreator
    public RestaurantSearchRequest(
            @JsonProperty("keyword") String keyword,
            @JsonProperty("currentPage") Integer currentPage,
            @JsonProperty("size") Integer size) {
        super(size, currentPage);
        this.keyword = keyword;
    }
}
