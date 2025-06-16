package com.moyorak.infra.orm;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.moyorak.api.auth.domain.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

class SecurityAuditorAwareTest {

    private final SecurityAuditorAware auditorAware = new SecurityAuditorAware();

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("인증 정보가 없을 경우 기본 값으로 반환됩니다.")
    void isNotAuthentication() {
        // given
        final Long expectedResult = 0L;

        SecurityContextHolder.createEmptyContext();

        // when
        final Long result = auditorAware.getCurrentAuditor().get();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("인증에 실패했을 경우에는 기본 값으로 반환합니다.")
    void isUnauthenticated() {
        // given
        final Long expectedResult = 0L;

        final Long userId = 99L;
        final UserPrincipal userPrincipal = UserPrincipal.generate(userId, "", "");
        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, "");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final Long result = auditorAware.getCurrentAuditor().get();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("인증에 성공했을 경우, 인증 요청한 회원의 ID가 정상적으로 반환됩니다.")
    void success() {
        // given
        final Long userId = 99L;
        final UserPrincipal userPrincipal = UserPrincipal.generate(userId, "", "");
        final Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userPrincipal, "", userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        final Long result = auditorAware.getCurrentAuditor().get();

        // then
        assertThat(result).isEqualTo(userId);
    }
}
