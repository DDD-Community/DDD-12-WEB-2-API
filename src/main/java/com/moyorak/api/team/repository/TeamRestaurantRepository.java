package com.moyorak.api.team.repository;

import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.api.team.dto.TeamRestaurantSearchSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TeamRestaurantRepository extends CrudRepository<TeamRestaurant, Long> {
    @EntityGraph(attributePaths = "restaurant")
    Optional<TeamRestaurant> findByTeamIdAndIdAndUse(Long teamId, Long id, boolean use);

    Optional<TeamRestaurant> findByTeamIdAndRestaurantIdAndUse(
            Long teamId, Long restaurantId, boolean use);

    @Query(
            """
SELECT new com.moyorak.api.team.dto.TeamRestaurantSearchSummary(tr.id, r.name, r.category, tr.averageReviewScore, tr.reviewCount)
FROM TeamRestaurant tr
JOIN tr.restaurant r
WHERE tr.id IN :ids AND tr.use = :use
""")
    List<TeamRestaurantSearchSummary> findByIdInAndUse(List<Long> ids, boolean use);
}
