package com.moyorak.api.team.repository;

import com.moyorak.api.team.domain.TeamPlace;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamPlaceRepository extends JpaRepository<TeamPlace, Long> {

    Optional<TeamPlace> findByTeamIdAndRestaurantIdAndUse(
            Long teamId, Long restaurantId, boolean use);
}
