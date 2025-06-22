package com.moyorak.api.restaurant.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.moyorak.api.helper.TestFixtureUtils;
import com.moyorak.api.restaurant.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {

    private static final String BASE_URL = "/api/restaurants";
    private MockMvc mockMvc;

    @Mock private RestaurantService restaurantService;

    @BeforeEach
    void setup() {
        RestaurantController controller = new RestaurantController(restaurantService);

        mockMvc =
                MockMvcBuilders.standaloneSetup(controller) // Spring 없이 순수하게 컨트롤러만 테스트
                        .addFilters(new CharacterEncodingFilter("UTF-8", true))
                        .build();
    }

    @Nested
    @DisplayName("식당 저장 요청 시")
    class Save {

        @Nested
        @DisplayName("kakaPlaceId가")
        class KakaPlaceId {
            @Test
            @DisplayName("빈 문자열이면 400을 반환합니다.")
            void isEmpty() throws Exception {
                final String requestBody =
                        """
                    {
                      "kakaoPlaceId": "",
                      "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                      "name": "우가우가 차차차",
                      "address": "우가우가시 차차차동 24번길",
                      "category": "KOREAN",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("공백 문자열이면 400을 반환합니다.")
            void isBlackSpace() throws Exception {
                final String requestBody =
                        """
                    {
                      "kakaoPlaceId": "     ",
                      "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                      "name": "우가우가 차차차",
                      "address": "우가우가시 차차차동 24번길",
                      "category": "KOREAN",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("null이면 400을 반환합니다.")
            void isNull() throws Exception {
                final String requestBody =
                        """
                    {
                      "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                      "name": "우가우가 차차차",
                      "address": "우가우가시 차차차동 24번길",
                      "category": "KOREAN",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("30자를 초과하면 400을 반환합니다.")
            void isTooLong() throws Exception {
                final String kakaoPlaceId = TestFixtureUtils.createStringWithLength(31);
                final String requestBody =
                        """
                    {
                      "kakaoPlaceId": "%s",
                      "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                      "name": "우가우가 차차차",
                      "address": "우가우가시 차차차동 24번길",
                      "category": "KOREAN",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """
                                .formatted(kakaoPlaceId);

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("kakaoPlaceUrl가")
        class KakaPlaceUrl {
            @Test
            @DisplayName("512자를 초과하면 400을 반환합니다.")
            void isTooLong() throws Exception {
                final String kakaoPlaceUrl = TestFixtureUtils.createStringWithLength(513);
                final String requestBody =
                        """
                    {
                      "kakaoPlaceId": "12345",
                      "kakaoPlaceUrl": "%s",
                      "name": "우가우가 차차차",
                      "address": "우가우가시 차차차동 24번길",
                      "category": "KOREAN",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """
                                .formatted(kakaoPlaceUrl);

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("식당 이름이")
        class Name {
            @Test
            @DisplayName("255자를 초과하면 400을 반환합니다.")
            void isTooLong() throws Exception {
                final String name = TestFixtureUtils.createStringWithLength(256);
                final String requestBody =
                        """
                    {
                      "kakaoPlaceId": "12345",
                      "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                      "name": "%s",
                      "address": "우가우가시 차차차동 24번길",
                      "category": "KOREAN",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """
                                .formatted(name);

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("식당 주소가")
        class Address {
            @Test
            @DisplayName("255자를 초과하면")
            void isTooLong() throws Exception {
                final String address = TestFixtureUtils.createStringWithLength(256);
                final String requestBody =
                        """
                    {
                      "kakaoPlaceId": "12345",
                      "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                      "name": "우가우가 차차차",
                      "address": "%s",
                      "category": "KOREAN",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """
                                .formatted(address);

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("삭당 카테고리가")
        class Category {
            @Test
            @DisplayName("null이면 400을 반환합니다.")
            void isNull() throws Exception {
                final String requestBody =
                        """
                    {
                      "kakaoPlaceId": "12345",
                      "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                      "name": "우가우가 차차차",
                      "address": "우가우가시 차차차동 24번길",
                      "longitude": 127.043616,
                      "latitude": 37.503095
                    }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .content(requestBody)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("경도가")
        class Longitude {
            @Test
            @DisplayName("null이면 400을 반환합니다.")
            void isNull() throws Exception {
                final String request =
                        """
                        {
                          "kakaoPlaceId": "123456",
                          "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                          "name": "식당이름",
                          "address": "서울시 어딘가",
                          "category": "KOREAN",
                          "latitude": 37.503095
                        }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("-180이하이면 400 반환")
            void isTooLow() throws Exception {
                final String request =
                        """
                        {
                          "kakaoPlaceId": "123456",
                          "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                          "name": "식당이름",
                          "address": "서울시 어딘가",
                          "category": "KOREAN",
                          "longitude": -180.1,
                          "latitude": 37.503095
                        }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("180이상이면 400반환")
            void isTooHigh() throws Exception {
                final String request =
                        """
                        {
                          "kakaoPlaceId": "123456",
                          "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                          "name": "식당이름",
                          "address": "서울시 어딘가",
                          "category": "KOREAN",
                          "longitude": 180.1,
                          "latitude": 37.503095
                        }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("위도가")
        class Latitude {

            @Test
            @DisplayName("null이면 400을 반환합니다.")
            void isNull() throws Exception {
                final String request =
                        """
                        {
                          "kakaoPlaceId": "123456",
                          "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                          "name": "식당이름",
                          "address": "서울시 어딘가",
                          "category": "KOREAN",
                          "longitude": 127.043616
                        }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("-90 이하이면 400 반환")
            void isTooLow() throws Exception {
                final String request =
                        """
                        {
                          "kakaoPlaceId": "123456",
                          "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                          "name": "식당이름",
                          "address": "서울시 어딘가",
                          "category": "KOREAN",
                          "longitude": 127.043616,
                          "latitude": -90.1
                        }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isBadRequest());
            }

            @Test
            @DisplayName("90 이상이면 400 반환")
            void isTooHigh() throws Exception {
                final String request =
                        """
                        {
                          "kakaoPlaceId": "123456",
                          "kakaoPlaceUrl": "http://place.map.kakao.com/123456",
                          "name": "식당이름",
                          "address": "서울시 어딘가",
                          "category": "KOREAN",
                          "longitude": 127.043616,
                          "latitude": 90.1
                        }
                    """;

                mockMvc.perform(
                                post(BASE_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(request))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
