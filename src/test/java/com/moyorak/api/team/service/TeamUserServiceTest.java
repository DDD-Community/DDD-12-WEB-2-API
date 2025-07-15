package com.moyorak.api.team.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.moyorak.api.team.domain.TeamRole;
import com.moyorak.api.team.domain.TeamUser;
import com.moyorak.api.team.domain.TeamUserFixture;
import com.moyorak.api.team.domain.TeamUserNotFoundException;
import com.moyorak.api.team.domain.TeamUserStatus;
import com.moyorak.api.team.repository.TeamUserRepository;
import com.moyorak.config.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TeamUserServiceTest {

    @InjectMocks private TeamUserService teamUserService;

    @Mock private TeamUserRepository teamUserRepository;

    @Nested
    @DisplayName("withdraw 호출 시")
    class Withdraw {

        final Long teamId = 1L;
        final Long userId = 1L;

        @Test
        @DisplayName("팀유저가 아닌 경우 TeamUserNotFoundException이 발생한다.")
        void isNotExist() {
            // given
            given(teamUserRepository.findByUserIdAndTeamIdAndUse(userId, teamId, true))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamUserService.withdraw(userId, teamId))
                    .isInstanceOf(TeamUserNotFoundException.class)
                    .hasMessage("팀 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("팀유저 상태가 APPROVED가 아닌 경우 TeamUserNotFoundException이 발생한다.")
        void isNotApproved() {
            // given
            final TeamUser pendingUser =
                    TeamUserFixture.fixture(TeamUserStatus.PENDING, TeamRole.TEAM_MEMBER, true);

            given(teamUserRepository.findByUserIdAndTeamIdAndUse(userId, teamId, true))
                    .willReturn(Optional.of(pendingUser));

            // when & then
            assertThatThrownBy(() -> teamUserService.withdraw(userId, teamId))
                    .isInstanceOf(TeamUserNotFoundException.class)
                    .hasMessage("팀 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("팀 관리자인 경우 BusinessException이 발생한다.")
        void isAdmin() {
            // given
            final TeamUser teamAdminUser =
                    TeamUserFixture.fixture(TeamUserStatus.APPROVED, TeamRole.TEAM_ADMIN, true);

            given(teamUserRepository.findByUserIdAndTeamIdAndUse(userId, teamId, true))
                    .willReturn(Optional.of(teamAdminUser));

            // when & then
            assertThatThrownBy(() -> teamUserService.withdraw(userId, teamId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 관리자는 탈퇴할 수 없습니다.");
        }
    }
}
