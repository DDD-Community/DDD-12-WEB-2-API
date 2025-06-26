package com.moyorak.infra.client.kakao.dto;

import java.util.List;

public record KakaoSearchResponse(List<KakaoPlace> documents, KakaoMeta meta) {}
