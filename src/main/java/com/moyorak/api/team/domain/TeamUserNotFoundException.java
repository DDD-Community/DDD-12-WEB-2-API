package com.moyorak.api.team.domain;

import com.moyorak.config.exception.BusinessException;

public class TeamUserNotFoundException extends BusinessException {
    public TeamUserNotFoundException() {
        super("팀 멤버가 아닙니다.");
    }
}
