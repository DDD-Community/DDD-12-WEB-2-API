package com.moyorak.api.team.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TeamInvitationTest {

    @Test
    @DisplayName("토큰 만료가 되면 true를 반환한다.")
    void isExpired() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expiresDate = now.minusSeconds(1);
        final TeamInvitation teamInvitation =
                TeamInvitationFixture.fixture(1L, "token", expiresDate, 1L);

        // when
        final boolean result = teamInvitation.isExpired(now);

        // then
        assertThat(result).isTrue();
    }
}
