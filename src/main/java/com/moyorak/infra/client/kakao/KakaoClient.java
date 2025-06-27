package com.moyorak.infra.client.kakao;

import com.moyorak.config.exception.BusinessException;
import com.moyorak.config.exception.ServerSideException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
public class KakaoClient {

    private final RestClient restClient;

    public <T> T get(String uri, ParameterizedTypeReference<T> typeReference) {
        return restClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        ((request, response) -> {
                            log.error("Kakao 4xx Error Code {}", response.getStatusCode());
                            throw new BusinessException("잘못된 요청입니다.");
                        }))
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        ((request, response) -> {
                            log.error("Kakao 5xx Error Code {}", response.getStatusCode());
                            throw new ServerSideException("서버 오류가 발생했습니다.");
                        }))
                .body(typeReference);
    }
}
