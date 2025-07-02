package com.moyorak.infra.orm;

import com.moyorak.api.team.domain.TeamUserStatus;
import jakarta.persistence.Converter;

@Converter
public class TeamUserStatusConverter extends AbstractCommonEnumAttributeConverter<TeamUserStatus> {

    public TeamUserStatusConverter() {
        super(TeamUserStatus.class);
    }
}
