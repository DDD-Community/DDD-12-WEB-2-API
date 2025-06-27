package com.moyorak.infra.client.kakao;

import com.moyorak.infra.client.kakao.dto.KakaoPlace;
import com.moyorak.infra.client.kakao.dto.KakaoSearchRequest;
import com.moyorak.infra.client.kakao.dto.KakaoSearchResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoSearcher {

    private final KakaoClient kakaoClient;
    private static final String KEYWORD_SEARCH_PATH = "/v2/local/search/keyword.json";

    public Page<KakaoPlace> search(KakaoSearchRequest kakaoSearchRequest) {
        String uri =
                UriComponentsBuilder.fromPath(KEYWORD_SEARCH_PATH)
                        .queryParams(kakaoSearchRequest.toMultiValueMap())
                        .build()
                        .toUriString();
        KakaoSearchResponse kakaoSearchResponse =
                kakaoClient.get(uri, new ParameterizedTypeReference<>() {});
        return createPage(
                kakaoSearchResponse, kakaoSearchRequest.page(), kakaoSearchRequest.size());
    }

    private Page<KakaoPlace> createPage(
            KakaoSearchResponse kakaoSearchResponse, int currentPage, int size) {
        int offset = (currentPage - 1) * size;
        PageRequest pageRequest = PageRequest.of(currentPage - 1, size);
        if (offset >= kakaoSearchResponse.meta().pageableCount()) {
            return new PageImpl<>(
                    Collections.emptyList(),
                    pageRequest,
                    kakaoSearchResponse.meta().pageableCount());
        }
        return new PageImpl<>(
                kakaoSearchResponse.documents(),
                pageRequest,
                kakaoSearchResponse.meta().pageableCount());
    }
}
