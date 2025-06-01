package com.moyorak.api.restaurant.external.dto;

import java.util.Objects;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public record KakaoSearchRequest(
        String query,
        Double latitude,
        Double longitude,
        Integer radius,
        Integer page,
        Integer size,
        String categoryGroupCode) {

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("query", query);
        addIfNotNull(params, "x", longitude);
        addIfNotNull(params, "y", latitude);
        addIfNotNull(params, "radius", radius);
        addIfNotNull(params, "page", page);
        addIfNotNull(params, "size", size);
        addIfNotNull(params, "category_group_code", categoryGroupCode);
        return params;
    }

    private void addIfNotNull(MultiValueMap<String, String> params, String key, Object value) {
        if (Objects.nonNull(value)) {
            params.add(key, String.valueOf(value));
        }
    }
}
