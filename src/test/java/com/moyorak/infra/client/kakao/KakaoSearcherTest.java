package com.moyorak.infra.client.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.moyorak.infra.client.kakao.dto.KakaoMeta;
import com.moyorak.infra.client.kakao.dto.KakaoPlace;
import com.moyorak.infra.client.kakao.dto.KakaoSearchRequest;
import com.moyorak.infra.client.kakao.dto.KakaoSearchResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class KakaoSearcherTest {

    @Mock private KakaoClient kakaoClient;

    @InjectMocks private KakaoSearcher kakaoSearcher;

    private static final String KEYWORD_SEARCH_PATH = "/v2/local/search/keyword.json";

    @Nested
    @DisplayName("카카오 api로 데이터를 조회할 때")
    class Search {
        @Test
        @DisplayName("제공 가능한 데이터의 마지막 페이지를 초과하면 빈 리스트를 반환합니다.")
        void isOverLimit() {
            // given
            final int page = 6;
            final int size = 10;
            final KakaoSearchRequest request =
                    new KakaoSearchRequest("김밥", null, null, null, page, size, null);

            final int maxAvailableResults = 45; // 실제 카카오가 제공 가능한 결과 수
            final int totalCount = 500; // 카카오의 전체 데이터 수

            final KakaoSearchResponse kakaoSearchResponse =
                    new KakaoSearchResponse(
                            List.of(), new KakaoMeta(totalCount, maxAvailableResults, true));

            final String uri =
                    UriComponentsBuilder.fromPath(KEYWORD_SEARCH_PATH)
                            .queryParams(request.toMultiValueMap())
                            .build()
                            .toUriString();

            given(kakaoClient.get(uri, new ParameterizedTypeReference<KakaoSearchResponse>() {}))
                    .willReturn(kakaoSearchResponse);

            // when
            Page<KakaoPlace> result = kakaoSearcher.search(request);

            // then
            assertThat(result.getContent()).isNotNull().isEmpty();
        }
    }
}
