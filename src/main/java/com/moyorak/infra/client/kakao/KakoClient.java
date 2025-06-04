package com.moyorak.infra.client.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Slf4j
public class KakoClient {

    private final RestClient restClient;

    public <T> T get(String uri, ParameterizedTypeReference<T> typeReference) {
        return restClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        ((request, response) -> {
                            log.error("Kakao Error Code {}", response.getStatusCode());
                            throw new RuntimeException("Kakao Client Error");
                        }))
                .body(typeReference);
    }
}
