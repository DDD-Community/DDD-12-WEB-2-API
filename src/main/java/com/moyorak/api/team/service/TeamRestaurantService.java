package com.moyorak.api.team.service;

import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.domain.TeamRestaurantNotFoundException;
import com.moyorak.api.team.dto.TeamRestaurantResponse;
import com.moyorak.api.team.repository.TeamRestaurantRepository;
import com.moyorak.config.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamRestaurantService {
    private final TeamRestaurantRepository teamRestaurantRepository;

    @Transactional(readOnly = true)
    public TeamRestaurantResponse getTeamRestaurant(Long teamId, Long teamRestaurantId) {
        final TeamRestaurant teamRestaurant =
                teamRestaurantRepository
                        .findByTeamIdAndId(teamId, teamRestaurantId)
                        .orElseThrow(TeamRestaurantNotFoundException::new);
        if (teamRestaurant.getRestaurant() == null) {
            throw new BusinessException("연결된 식당 정보가 존재하지 않습니다.");
        }

        return TeamRestaurantResponse.from(teamRestaurant);
    }
}
