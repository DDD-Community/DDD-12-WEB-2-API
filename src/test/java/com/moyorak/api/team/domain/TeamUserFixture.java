package com.moyorak.api.team.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class TeamUserFixture {

    public static TeamUser fixture(
            final Long userId, final Team team, final TeamUserStatus status, boolean use) {
        TeamUser teamUser = new TeamUser();

        ReflectionTestUtils.setField(teamUser, "id", userId);
        ReflectionTestUtils.setField(teamUser, "team", team);
        ReflectionTestUtils.setField(teamUser, "status", status);
        ReflectionTestUtils.setField(teamUser, "use", use);

        return teamUser;
    }

    public static TeamUser fixtureApproved(final Long userId, final Team team) {
        return fixture(userId, team, TeamUserStatus.APPROVED, true);
    }
}
