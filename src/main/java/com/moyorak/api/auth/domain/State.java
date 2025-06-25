package com.moyorak.api.auth.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.util.StringUtils;

public enum State {
    ON,
    OFF;

    @JsonCreator
    public static State from(final String input) {
        if (!StringUtils.hasText(input)) {
            return null;
        }

        for (State state : State.values()) {
            if (input.equals(state.name())) {
                return state;
            }
        }

        return null;
    }
}
