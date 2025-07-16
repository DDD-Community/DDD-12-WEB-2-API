package com.moyorak.api.team.domain;

import com.moyorak.config.exception.BusinessException;

public class TeamInvitationTokenNotFoundException extends BusinessException {
    public TeamInvitationTokenNotFoundException() {
        super("토큰이 존재하지 않습니다.");
    }
}
