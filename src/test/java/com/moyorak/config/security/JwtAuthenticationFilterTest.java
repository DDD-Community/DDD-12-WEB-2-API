package com.moyorak.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyorak.api.auth.domain.UserPrincipal;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtAuthenticationFilterTest {

    private static final String JWT_TOKEN_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider =
            new JwtTokenProvider(new JwtTokenProperties("this-is-a-longer-test-secret-key-123456"));
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CustomAuthenticationEntryPoint authenticationEntryPoint =
            new CustomAuthenticationEntryPoint(objectMapper);

    private final JwtAuthenticationFilter jwtAuthenticationFilter =
            new JwtAuthenticationFilter(jwtTokenProvider, authenticationEntryPoint);

    @Nested
    @DisplayName("요청의 토큰이 유효한지 검증할 때,")
    class requestCheck {

        @Test
        @DisplayName("토큰이 비어 있으면, 401를 반환합니다.")
        void isEmpty() throws ServletException, IOException {
            // given
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();

            // when
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(response.getStatus())
                                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
                        it.assertThat(getResponseMessage(response)).contains("로그인 정보가 없습니다.");
                    });
        }

        @Test
        @DisplayName("토큰이 유효한 prefix로 시작하지 않는다면, 401를 반환합니다.")
        void invalidToken() throws ServletException, IOException {
            // given
            final String token = String.join("", "INVALID-PREFIX ", "INVALID-TOKEN");

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(HttpHeaders.AUTHORIZATION, token);

            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();

            // when
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(response.getStatus())
                                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
                        it.assertThat(getResponseMessage(response)).contains("잘못된 요청입니다.");
                    });
        }

        @Test
        @DisplayName("유효하지 않은 토큰으로 요청하게 되면, 401를 반환합니다.")
        void isValid() throws ServletException, IOException {
            // given
            final String token = String.join("", JWT_TOKEN_PREFIX, "INVALID-TOKEN");

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(HttpHeaders.AUTHORIZATION, token);

            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();

            // when
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(response.getStatus())
                                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
                        it.assertThat(getResponseMessage(response)).contains("잘못된 요청입니다.");
                    });
        }

        @Test
        @DisplayName("유효한 토큰이 들어왔을 때, 인증에 성공합니다.")
        void success() throws ServletException, IOException {
            // given
            final String token =
                    String.join(
                            "",
                            JWT_TOKEN_PREFIX,
                            jwtTokenProvider.generateAccessToken(
                                    UserPrincipal.generate(1L, "test@test.com", "홍길동")));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(HttpHeaders.AUTHORIZATION, token);

            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();

            // when
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // then
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }
    }

    @Nested
    @DisplayName("특정 URI 경우 JWT 필터에 적용되지 않게 할 때,")
    class shouldNotFilter {

        @Test
        @DisplayName("예외가 포함 되어 있으면 성공적으로 제외 된다.")
        void isTrue() {
            // given
            MockHttpServletRequest request =
                    new MockHttpServletRequest("POST", "/api/auth/sign-in");

            // when
            final Boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("예외에 포함되지 않을 경우 필터 처리를 위해 false가 응답 된다.")
        void isFalse() {
            MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/sample");

            // when
            final Boolean result = jwtAuthenticationFilter.shouldNotFilter(request);

            // then
            assertThat(result).isFalse();
        }
    }

    private static String getResponseMessage(MockHttpServletResponse response) {
        try {
            return response.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
