package com.moyorak.api.team.domain;

import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class TeamInvitationFixture {

    public static TeamInvitation fixture(
            Long id, String token, LocalDateTime expiresDate, Long teamId) {
        TeamInvitation teamInvitation = new TeamInvitation();
        ReflectionTestUtils.setField(teamInvitation, "id", id);
        ReflectionTestUtils.setField(teamInvitation, "invitationToken", token);
        ReflectionTestUtils.setField(teamInvitation, "expiresDate", expiresDate);
        ReflectionTestUtils.setField(teamInvitation, "teamId", teamId);
        return teamInvitation;
    }
}
