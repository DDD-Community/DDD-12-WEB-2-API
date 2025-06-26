package com.moyorak.infra.client.kakao.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class KakaoSearchResponseTest {

    private final ObjectMapper objectMapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    @DisplayName("KakaoSearchResponse 역직렬화")
    void success() throws JsonProcessingException {

        // given
        String json =
                """
            {
              "meta": {
                "same_name": {
                  "region": [],
                  "keyword": "카카오프렌즈",
                  "selected_region": ""
                },
                "pageable_count": 14,
                "total_count": 14,
                "is_end": true
              },
              "documents": [
                {
                  "place_name": "카카오프렌즈 코엑스점",
                  "distance": "418",
                  "place_url": "http://place.map.kakao.com/26338954",
                  "category_name": "가정,생활 > 문구,사무용품 > 디자인문구 > 카카오프렌즈",
                  "address_name": "서울 강남구 삼성동 159",
                  "road_address_name": "서울 강남구 영동대로 513",
                  "id": "26338954",
                  "phone": "02-6002-1880",
                  "category_group_code": "",
                  "category_group_name": "",
                  "x": "127.05902969025047",
                  "y": "37.51207412593136"
                }
              ]
            }
            """;

        // when
        KakaoSearchResponse kakaoSearchResponse =
                objectMapper.readValue(json, KakaoSearchResponse.class);

        assertThat(kakaoSearchResponse.meta().pageableCount()).isEqualTo(14);
        assertThat(kakaoSearchResponse.meta().totalCount()).isEqualTo(14);
        assertThat(kakaoSearchResponse.meta().isEnd()).isTrue();

        KakaoPlace kakaoPlace = kakaoSearchResponse.documents().get(0);
        assertThat(kakaoPlace.placeName()).isEqualTo("카카오프렌즈 코엑스점");
        assertThat(kakaoPlace.placeUrl()).isEqualTo("http://place.map.kakao.com/26338954");
        assertThat(kakaoPlace.addressName()).isEqualTo("서울 강남구 삼성동 159");
        assertThat(kakaoPlace.roadAddressName()).isEqualTo("서울 강남구 영동대로 513");
        assertThat(kakaoPlace.x()).isEqualTo(Double.valueOf("127.05902969025047"));
        assertThat(kakaoPlace.y()).isEqualTo(Double.valueOf("37.51207412593136"));
    }
}
