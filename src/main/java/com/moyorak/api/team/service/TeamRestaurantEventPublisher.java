package com.moyorak.api.team.service;

import com.moyorak.api.team.dto.TeamRestaurantSearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamRestaurantEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishSearchEvent(final Long userId, final Long teamId, final String keyword) {
        publisher.publishEvent(TeamRestaurantSearchEvent.of(userId, teamId, keyword));
    }
}
