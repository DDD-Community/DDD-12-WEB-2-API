package com.moyorak.api.team.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record InvitationToken(String token, LocalDateTime expiresDate) {
    private static final int INVITATION_VALIDITY_SECONDS = 60 * 60 * 24; // 일단 하루 (추후에 정하기)

    public static InvitationToken generate() {
        final String token = UUID.randomUUID().toString();
        final LocalDateTime expiresDate =
                LocalDateTime.now().plusSeconds(INVITATION_VALIDITY_SECONDS);
        return new InvitationToken(token, expiresDate);
    }
}
