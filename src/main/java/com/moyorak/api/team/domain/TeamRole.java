package com.moyorak.api.team.domain;

import com.moyorak.infra.orm.CommonEnum;
import lombok.Getter;

@Getter
public enum TeamRole implements CommonEnum {
    TEAM_ADMIN("관리자"),
    TEAM_MEMBER("팀원");

    private final String description;

    TeamRole(String description) {
        this.description = description;
    }
}
