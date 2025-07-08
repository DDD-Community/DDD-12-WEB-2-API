package com.moyorak.api.team.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TeamSearchRequestTest {

    @Nested
    @DisplayName("입력 값이 최소 하나는 필수인 것을 검증할 때,")
    class hasAtLeastOneCondition {

        @Test
        @DisplayName("입력 값 모두 null이면 false를 반환한다.")
        void isNull() {
            // given
            final TeamSearchRequest request = new TeamSearchRequest(null, null);

            // when
            final boolean result = request.hasAtLeastOneCondition();

            // then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("팀 고유 ID만 존재하여도 true를 반환한다.")
        void isIdNotNull() {
            // given
            final Long teamId = 1L;
            final TeamSearchRequest request = new TeamSearchRequest(teamId, null);

            // when
            final boolean result = request.hasAtLeastOneCondition();

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("팀 이름만 존재하여도 true를 반환한다.")
        void isIdNameNull() {
            // given
            final String name = "Backend";
            final TeamSearchRequest request = new TeamSearchRequest(null, name);

            // when
            final boolean result = request.hasAtLeastOneCondition();

            // then
            assertThat(result).isTrue();
        }
    }
}
