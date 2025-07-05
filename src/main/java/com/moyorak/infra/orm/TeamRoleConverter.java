package com.moyorak.infra.orm;

import com.moyorak.api.team.domain.TeamRole;
import jakarta.persistence.Converter;

@Converter
public class TeamRoleConverter extends AbstractCommonEnumAttributeConverter<TeamRole> {

    public TeamRoleConverter() {
        super(TeamRole.class);
    }
}
