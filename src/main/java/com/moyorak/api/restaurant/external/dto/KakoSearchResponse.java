package com.moyorak.api.restaurant.external.dto;

import java.util.List;

public record KakoSearchResponse(List<KakaoPlace> documents, KakaoMeta meta) {}
