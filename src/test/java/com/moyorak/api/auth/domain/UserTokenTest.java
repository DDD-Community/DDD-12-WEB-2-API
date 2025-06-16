package com.moyorak.api.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserTokenTest {

    @Nested
    @DisplayName("토큰이 유효하지 않은지 확인할 때,")
    class isInValidToken {

        @Test
        @DisplayName("토큰이 존재하면 유효하여 false를 반환합니다.")
        void validToken() {
            // given
            final String token = "EXAMPLE-TOKEN";
            final UserToken userToken = UserToken.create(1L, token);

            // when
            final boolean result = userToken.isInValidToken();

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("토큰이 존재하지 않은 경우, 유효하지 않기에 true를 반환합니다.")
        void invalidToken() {
            // given
            final String token = null;
            final UserToken userToken = UserToken.create(1L, token);

            // when
            final boolean result = userToken.isInValidToken();

            // then
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("토큰을 초기화합니다.")
    void clear() {
        // given
        final String token = "EXAMPLE-TOKEN";
        final UserToken userToken = UserToken.create(1L, token);

        // when
        userToken.clear();

        // then
        assertThat(userToken.getAccessToken()).isNull();
    }
}
