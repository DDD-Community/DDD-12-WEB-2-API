package com.moyorak.api.auth.repository;

import com.moyorak.api.auth.domain.FoodFlag;
import com.moyorak.api.auth.dto.FoodFlagTypeCount;
import jakarta.persistence.QueryHint;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FoodFlagRepository extends CrudRepository<FoodFlag, Long> {

    @Query(
            """
            SELECT new com.moyorak.api.auth.dto.FoodFlagTypeCount(f.type, COUNT(f))
            FROM FoodFlag f
            WHERE f.userId = :userId
            AND f.use = true
            GROUP BY f.type
        """)
    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value =
                            "FoodFlagRepository.findTypeCountByUserId : 각 타입별 아이템 갯수를 DTO 형식으로 조회합니다."))
    List<FoodFlagTypeCount> findTypeCountByUserId(@Param("userId") Long userId);
}
