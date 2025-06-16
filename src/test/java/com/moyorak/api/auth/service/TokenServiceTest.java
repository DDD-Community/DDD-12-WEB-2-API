package com.moyorak.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.auth.domain.UserFixture;
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

            given(userRepository.findById(userId))
                    .willThrow(new BusinessException("유효하지 않은 회원 ID 입니다."));

            // when & then
            assertThatThrownBy(() -> tokenService.generate(userId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("유효하지 않은 회원 ID 입니다.");
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
}
