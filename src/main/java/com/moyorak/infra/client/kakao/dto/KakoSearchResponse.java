package com.moyorak.infra.client.kakao.dto;

import java.util.List;

public record KakoSearchResponse(List<KakaoPlace> documents, KakaoMeta meta) {}
