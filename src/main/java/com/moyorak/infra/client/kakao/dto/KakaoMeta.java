package com.moyorak.infra.client.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMeta(
        @JsonProperty("total_count") int totalCount,
        @JsonProperty("pageable_count") int pageableCount,
        @JsonProperty("is_end") boolean isEnd) {}
