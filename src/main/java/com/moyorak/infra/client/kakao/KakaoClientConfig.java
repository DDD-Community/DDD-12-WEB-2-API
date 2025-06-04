package com.moyorak.infra.client.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
class KakaoClientConfig {

    private final RestClient.Builder restClientBuilder;

    @Bean
    KakoClient kakoClient(
            @Value("${kakao.api.base-url}") String baseUrl,
            @Value("${kakao.api.key}") String apiKey) {
        RestClient restClient =
                restClientBuilder
                        .baseUrl(baseUrl)
                        .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey)
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .build();
        return new KakoClient(restClient);
    }
}
