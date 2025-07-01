package com.moyorak.api.team.repository;

import com.moyorak.api.team.domain.TeamRestaurant;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface TeamRestaurantRepository extends CrudRepository<TeamRestaurant, Long> {
    @EntityGraph(attributePaths = "restaurant")
    Optional<TeamRestaurant> findByTeamIdAndIdAndUse(Long teamId, Long id, boolean use);
}
