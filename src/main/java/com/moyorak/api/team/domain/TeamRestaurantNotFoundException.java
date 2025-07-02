package com.moyorak.api.team.domain;

import com.moyorak.config.exception.BusinessException;

public class TeamRestaurantNotFoundException extends BusinessException {

    public TeamRestaurantNotFoundException() {
        super("해당 팀 맛집을 찾을 수 없습니다.");
    }
}
