package com.moyorak.api.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

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

    @Nested
    @DisplayName("입력 된 토큰과, 현재 토큰이 동일한지 확인할 때,")
    class isEqualsToken {

        @Test
        @DisplayName("입력된 토큰이 null이면, false를 반환합니다.")
        void isNull() {
            // given
            final String token = null;
            final UserToken userToken = UserToken.create(1L, "EXAMPLE-TOKEN");

            // when
            final boolean result = userToken.isEqualsToken(token);

            // then
            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = " ")
        @DisplayName("서로 토큰이 다르다면, false를 반환합니다.")
        void notEquals(final String input) {
            // given
            final UserToken userToken = UserToken.create(1L, "EXAMPLE-TOKEN");

            // when
            final boolean result = userToken.isEqualsToken(input);

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("동일한 경우, true를 반환합니다.")
        void isEquals() {
            // given
            final String token = "EXAMPLE-TOKEN";
            final UserToken userToken = UserToken.create(1L, token);

            // when
            final boolean result = userToken.isEqualsToken(token);

            // then
            assertThat(result).isTrue();
        }
    }
}
