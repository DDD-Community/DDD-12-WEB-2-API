package com.moyorak.api.restaurant.repository;

import com.moyorak.api.restaurant.domain.RestaurantSearch;
import com.moyorak.api.restaurant.dto.RestaurantSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantSearchRepository extends JpaRepository<RestaurantSearch, Long> {

    @Query(
            value =
                    """
                        SELECT restaurant_id AS restaurantId,
                               name AS name,
                               road_address AS roadAddress
                        FROM restaurant_search
                        WHERE MATCH(name) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
                    """,
            countQuery =
                    """
                    SELECT COUNT(*) FROM restaurant_search
                    WHERE MATCH(name) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
                """,
            nativeQuery = true)
    Page<RestaurantSearchProjection> searchByKeyword(
            @Param("keyword") String keyword, Pageable pageable);
}
