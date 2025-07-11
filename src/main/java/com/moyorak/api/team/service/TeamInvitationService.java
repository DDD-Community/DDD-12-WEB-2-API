package com.moyorak.api.team.service;

import com.moyorak.api.team.domain.TeamInvitation;
import com.moyorak.api.team.domain.TeamUser;
import com.moyorak.api.team.domain.TeamUserNotFoundException;
import com.moyorak.api.team.dto.TeamInvitationCreateResponse;
import com.moyorak.api.team.repository.TeamInvitationRepository;
import com.moyorak.api.team.repository.TeamUserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamInvitationService {

    private final TeamInvitationRepository teamInvitationRepository;
    private final TeamUserRepository teamUserRepository;
    private static final int INVITATION_VALIDITY_SECONDS = 60 * 60 * 24; // 일단 하루 (추후에 정하기)

    // TODO 프론트에서 사용할 URL로 교체해야함 OR 그냥 토큰만 내려주기(프론트에서 전체 링크 생성)
    private static final String INVITATION_URL_PREFIX = "http://moyorak/invitation/";

    @Transactional
    public TeamInvitationCreateResponse createTeamInvitation(final Long teamId, final Long userId) {

        // 팀원인지 체크
        final TeamUser teamUser =
                teamUserRepository
                        .findByUserIdAndTeamIdAndUse(teamId, userId, true)
                        .orElseThrow(TeamUserNotFoundException::new);
        if (teamUser.isNotApproved()) {
            throw new TeamUserNotFoundException();
        }

        // 초대 토큰 생성
        final String invitationToken = UUID.randomUUID().toString();
        final LocalDateTime expiresDate =
                LocalDateTime.now().plusSeconds(INVITATION_VALIDITY_SECONDS);

        final TeamInvitation teamInvitation =
                TeamInvitation.create(invitationToken, expiresDate, teamId);
        teamInvitationRepository.save(teamInvitation);

        final String invitationUrl = INVITATION_URL_PREFIX + teamInvitation.getInvitationToken();
        return TeamInvitationCreateResponse.of(invitationUrl);
    }
}
