package com.moyorak.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.domain.UserFixture;
import com.moyorak.api.auth.domain.UserNotFoundException;
import com.moyorak.api.auth.domain.UserPrincipal;
import com.moyorak.api.auth.domain.UserToken;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks private TokenService tokenService;

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
            assertThatThrownBy(() -> tokenService.generate(userId))
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
            final UserToken expectedUserToken = UserToken.create(userId, token);

            given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
            given(jwtTokenProvider.generateAccessToken(any(UserPrincipal.class))).willReturn(token);
            given(tokenRepository.save(any(UserToken.class))).willReturn(expectedUserToken);

            // when
            final SignInResponse result = tokenService.generate(userId);

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
            assertThatThrownBy(() -> tokenService.signOut(userId))
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
        assertThatThrownBy(() -> tokenService.signOut(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("로그인 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("가장 최근 로그인 정보의 토큰이 null인 경우, 로그아웃 된 상태로 에러가 발생합니다.")
    void isSignOut() {
        // given
        final Long userId = 10L;

        final User expectedUser = UserFixture.fixture(userId, "", "", "");
        final UserToken expectedUserToken = UserToken.create(userId, null);

        given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
        given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                .willReturn(Optional.of(expectedUserToken));

        // when & then
        assertThatThrownBy(() -> tokenService.signOut(userId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("로그인 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("성공적으로 저장 된 토큰을 null로 업데이트합니다.")
    void success() {
        // given
        final Long userId = 10L;

        final User expectedUser = UserFixture.fixture(userId, "", "", "");
        final UserToken expectedUserToken = UserToken.create(userId, "EXAMPLE-TOKEN");

        given(userRepository.findById(userId)).willReturn(Optional.of(expectedUser));
        given(tokenRepository.findFirstByUserIdOrderByIdDesc(userId))
                .willReturn(Optional.of(expectedUserToken));

        // when
        tokenService.signOut(userId);

        // then
        assertThat(expectedUserToken.getAccessToken()).isNull();
    }
}
