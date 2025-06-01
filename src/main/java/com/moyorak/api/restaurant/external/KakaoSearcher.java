package com.moyorak.api.restaurant.external;

import com.moyorak.api.restaurant.external.dto.KakaoSearchRequest;
import com.moyorak.api.restaurant.external.dto.KakoSearchResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoSearcher {

    private final RestClient kakaoRestClient;

    public KakoSearchResponse search(KakaoSearchRequest kakaoSearchRequest) {
        URI uri =
                UriComponentsBuilder.fromPath("/v2/local/search/keyword.json")
                        .queryParams(kakaoSearchRequest.toMultiValueMap())
                        .build()
                        .toUri();

        return kakaoRestClient.get().uri(uri).retrieve().body(KakoSearchResponse.class);
    }
}
