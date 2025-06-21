package com.moyorak.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.auth.domain.InvalidTokenException;
import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.domain.UserFixture;
import com.moyorak.api.auth.domain.UserNotFoundException;
import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.domain.UserToken;
import com.moyorak.api.auth.domain.UserTokenFixture;
import com.moyorak.api.auth.dto.RefreshResponse;
import com.moyorak.api.auth.dto.SignInResponse;
import com.moyorak.api.auth.repository.TokenRepository;
import com.moyorak.api.auth.repository.UserRepository;
import com.moyorak.config.exception.BusinessException;
import com.moyorak.config.security.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks private AuthService authService;

    @Mock private TokenRepository tokenRepository;

    @Mock private UserRepository userRepository;

    @Mock private JwtTokenProvider jwtTokenProvider;

    @Nested
    @DisplayName("토큰을 발급 받을 때,")
    class generate {

        @Test
        @DisplayName("유효하지 않은 회원 ID의 경우 에러가 발생합니다.")
        void invalidUserId() {
            // given
            final Long userId = 1L;

            given(userRepository.findById(userId)).willThrow(new UserNotFoundException());

            // when & then
            assertThatThrownBy(() -> authService.generate(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("유효하지 않은 회원 정보입니다.");
        }

        @Test
        @DisplayName("성공적으로 발급 받습니다.")
        void success() {
            // given
            final Long userId = 1L;
            final String email = "gildong@gmail.com";
            final String name = "홍길동";

            final User expectedUser = UserFixture.fixture(userId, email, name, "");
            final String token = "TOKEN-EXAMPLE";
            final UserToken expectedUserToken = UserTokenFixture.fixture(userId, token);

            given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
            given(jwtTokenProvider.generateAccessToken(any(UserPrincipal.class))).willReturn(token);
            given(tokenRepository.save(any(UserToken.class))).willReturn(expectedUserToken);

            // when
            final SignInResponse result = authService.generate(userId);

            // then
            assertThat(result.accessToken()).isEqualTo(token);
        }
    }

    @Nested
    @DisplayName("로그아웃 할 때,")
    class signOut {

        @Test
        @DisplayName("유효하지 않은 회원 ID의 경우 에러가 발생합니다.")
        void invalidUserId() {
            // given
            final Long userId = 10L;

            given(userRepository.findById(userId)).willThrow(new UserNotFoundException());

            // when & then
            assertThatThrownBy(() -> authService.signOut(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("유효하지 않은 회원 정보입니다.");
        }
    }

    @Test
    @DisplayName("회원 정보는 존재하나, 로그인 정보가 존재하지 않는 경우, 에러가 발생합니다.")
    void invalidUserToken() {
        // given
        final Long userId = 10L;

        final User expectedUser = UserFixture.fixture(userId, "", "", "");

        given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
        given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                .willThrow(new BusinessException("로그인 정보가 존재하지 않습니다."));

        // when & then
        assertThatThrownBy(() -> authService.signOut(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("로그인 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("가장 최근 로그인 정보의 토큰이 null인 경우, 로그아웃 된 상태로 에러가 발생합니다.")
    void isSignOut() {
        // given
        final Long userId = 10L;

        final User expectedUser = UserFixture.fixture(userId, "", "", "");
        final UserToken expectedUserToken = UserTokenFixture.fixture(userId, null);

        given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
        given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                .willReturn(Optional.of(expectedUserToken));

        // when & then
        assertThatThrownBy(() -> authService.signOut(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("로그인 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("성공적으로 저장 된 토큰을 null로 업데이트합니다.")
    void success() {
        // given
        final Long userId = 10L;

        final User expectedUser = UserFixture.fixture(userId, "", "", "");
        final UserToken expectedUserToken = UserTokenFixture.fixture(userId, "EXAMPLE-TOKEN");

        given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
        given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                .willReturn(Optional.of(expectedUserToken));

        // when
        authService.signOut(userId);

        // then
        assertThat(expectedUserToken.getAccessToken()).isNull();
    }

    @Nested
    @DisplayName("입력받은 토큰이 저장 된 토큰과 같은지 비교할 때,")
    class validToken {

        @Test
        @DisplayName("회원이 존재하지 않으면 오류가 발생합니다.")
        void notFoundUser() {
            // given
            final Long userId = 1L;
            final String token = "EXAMPLE-TOKEN";

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authService.validToken(userId, token))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("유효하지 않은 회원 정보입니다.");
        }

        @Test
        @DisplayName("저장 된 토큰 정보가 없다면 오류가 발생합니다.")
        void notFoundToken() {
            // given
            final Long userId = 1L;
            final String token = "EXAMPLE-TOKEN";

            final User expectedUser = UserFixture.fixture(userId, "", "", "");

            given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
            given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> authService.validToken(userId, token))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("로그인 정보가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("동일하지 않으면 예외가 발생합니다.")
        void invalid() {
            // given
            final Long userId = 1L;
            final String token = "EXAMPLE-TOKEN";

            final User expectedUser = UserFixture.fixture(userId, "", "", "");
            final UserToken expectedUserToken = UserTokenFixture.fixture(userId, "X");

            given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
            given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                    .willReturn(Optional.of(expectedUserToken));

            // when & then
            assertThatThrownBy(() -> authService.validToken(userId, token))
                    .isInstanceOf(InvalidTokenException.class);
        }

        @Test
        @DisplayName("동일하면 예외가 발생하지 않습니다.")
        void valid() {
            // given
            final Long userId = 1L;
            final String token = "EXAMPLE-TOKEN";

            final User expectedUser = UserFixture.fixture(userId, "", "", "");
            final UserToken expectedUserToken = UserTokenFixture.fixture(userId, token);

            given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
            given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                    .willReturn(Optional.of(expectedUserToken));

            // when & then
            assertDoesNotThrow(() -> authService.validToken(userId, token));
        }
    }

    @Nested
    @DisplayName("토큰을 새로 갱신할 때,")
    class refresh {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = " ")
        @DisplayName("입력된 Refresh Token이 빈 값이면 오류가 발생합니다.")
        void isNull(final String input) {
            // when & then
            assertThatThrownBy(() -> authService.refresh(input))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("유효하지 않은 로그인 정보입니다.");
        }

        @Test
        @DisplayName("Refresh Token이 유효한지 확인할 때, 유효하지 않으면 오류가 발생합니다.")
        void isValidRefreshToken() {
            // given
            final String refreshToken = "INVALID-TOKEN";

            given(jwtTokenProvider.isValidRefreshToken(refreshToken)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> authService.refresh(refreshToken))
                    .isInstanceOf(InvalidTokenException.class)
                    .hasMessage("유효하지 않은 로그인 정보입니다.");
        }

        @Nested
        @DisplayName("회원이 유효한지 확인할 때,")
        class validUser {

            @Test
            @DisplayName("유효하지 않은 회원이면 오류가 발생합니다.")
            void invalidUser() {
                // given
                final String refreshToken = "VALID-TOKEN";

                final Long expectedUserId = 1L;

                given(jwtTokenProvider.isValidRefreshToken(refreshToken)).willReturn(true);
                given((jwtTokenProvider.getUserIdByRefreshToken(refreshToken)))
                        .willReturn(expectedUserId);
                given(userRepository.findById(expectedUserId)).willReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> authService.refresh(refreshToken))
                        .isInstanceOf(UserNotFoundException.class);
            }
        }

        @Nested
        @DisplayName("토큰 정보가 있는지 확인할 때,")
        class checkTokenInfo {

            @Test
            @DisplayName("토큰 정보가 없으면, 오류가 발생합니다.")
            void invalidTokenInfo() {
                // given
                final String refreshToken = "VALID-TOKEN";

                final Long expectedUserId = 1L;
                final User expectedUser = UserFixture.fixture(expectedUserId, "", "", "");

                given(jwtTokenProvider.isValidRefreshToken(refreshToken)).willReturn(true);
                given((jwtTokenProvider.getUserIdByRefreshToken(refreshToken)))
                        .willReturn(expectedUserId);
                given(userRepository.findById(expectedUserId))
                        .willReturn(Optional.of(expectedUser));
                given((tokenRepository.findFirstByUserIdOrderByIdDesc(expectedUserId)))
                        .willReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> authService.refresh(refreshToken))
                        .isInstanceOf(BusinessException.class)
                        .hasMessage("로그인 정보가 존재하지 않습니다.");
            }
        }

        @Nested
        @DisplayName("입력된 Refresh Token과 저장된 RefreshToken이 동일한지 비교할 때,")
        class isNotEquals {

            @Test
            @DisplayName("동일하지 않으면 오류가 발생합니다.")
            void notEquals() {
                // given
                final String refreshToken = "VALID-TOKEN";

                final Long expectedUserId = 1L;
                final User expectedUser = UserFixture.fixture(expectedUserId, "", "", "");
                final UserToken expectedUserToken =
                        UserTokenFixture.fixture(expectedUserId, "", "TOKEN");

                given(jwtTokenProvider.isValidRefreshToken(refreshToken)).willReturn(true);
                given((jwtTokenProvider.getUserIdByRefreshToken(refreshToken)))
                        .willReturn(expectedUserId);
                given(userRepository.findById(expectedUserId))
                        .willReturn(Optional.of(expectedUser));
                given((tokenRepository.findFirstByUserIdOrderByIdDesc(expectedUserId)))
                        .willReturn(Optional.of(expectedUserToken));

                // when & then
                assertThatThrownBy(() -> authService.refresh(refreshToken))
                        .isInstanceOf(InvalidTokenException.class)
                        .hasMessage("유효하지 않은 로그인 정보입니다.");
            }
        }

        @Test
        @DisplayName("토큰 재발급에 성공합니다.")
        void success() {
            // given
            final String refreshToken = "VALID-TOKEN";

            final String newToken = "NEW-VALID-TOKEN";
            final String newRefreshToken = "NEW-VALID-REFRESH-TOKEN";

            final Long expectedUserId = 1L;
            final User expectedUser = UserFixture.fixture(expectedUserId, "", "", "");
            final UserToken expectedUserToken =
                    UserTokenFixture.fixture(expectedUserId, "", refreshToken);

            given(jwtTokenProvider.isValidRefreshToken(refreshToken)).willReturn(true);
            given((jwtTokenProvider.getUserIdByRefreshToken(refreshToken)))
                    .willReturn(expectedUserId);
            given(userRepository.findById(expectedUserId)).willReturn(Optional.of(expectedUser));
            given((tokenRepository.findFirstByUserIdOrderByIdDesc(expectedUserId)))
                    .willReturn(Optional.of(expectedUserToken));
            given(jwtTokenProvider.generateAccessToken(any(UserPrincipal.class)))
                    .willReturn(newToken);
            given(jwtTokenProvider.generateRefreshToken(any(UserPrincipal.class)))
                    .willReturn(newRefreshToken);

            // when
            final RefreshResponse result = authService.refresh(refreshToken);

            // then
            assertSoftly(
                    it -> {
                        it.assertThat(result.accessToken()).isEqualTo(newToken);
                        it.assertThat(result.refreshToken()).isEqualTo(newRefreshToken);
                    });
        }
    }
}
