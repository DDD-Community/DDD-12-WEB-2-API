package com.moyorak.api.team.repository;

import com.moyorak.api.team.dto.SearchResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class TeamRestaurantSearchNativeRepository {

    private final EntityManager entityManager;
    private static final Map<String, String> SORT_COLUMN_MAPPING =
            Map.of(
                    "distanceFromTeam", "distance_from_team",
                    "averageReviewScore", "average_review_score",
                    "createdDate", "created_date");

    @Transactional(readOnly = true)
    public SearchResult searchByTeamIdAndName(Long teamId, String keyword, Pageable pageable) {

        // 정렬 문법
        StringBuilder orderClause = new StringBuilder();
        orderClause.append("ORDER BY MATCH(name) AGAINST(:keyword IN NATURAL LANGUAGE MODE) DESC");

        for (Sort.Order order : pageable.getSort()) {
            String column = SORT_COLUMN_MAPPING.get(order.getProperty());
            if (StringUtils.hasText(column)) {
                orderClause
                        .append(", ts.")
                        .append(SORT_COLUMN_MAPPING.get(order.getProperty()))
                        .append(" ")
                        .append(order.getDirection().name());
            }
        }
        // 본문 쿼리
        String sql =
                """
            SELECT ts.team_restaurant_id
            FROM team_restaurant_search ts
            WHERE MATCH(name) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
                AND ts.team_id = :teamId AND ts.use_yn = 'Y'
            """
                        + orderClause;

        Query query =
                entityManager
                        .createNativeQuery(sql, Long.class)
                        .setParameter("teamId", teamId)
                        .setParameter("keyword", keyword)
                        .setFirstResult((int) pageable.getOffset())
                        .setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<Long> ids = query.getResultList();

        // 카운트 쿼리
        String countSql =
                """
            SELECT COUNT(*)
            FROM team_restaurant_search ts
            WHERE MATCH(name) AGAINST(:keyword IN NATURAL LANGUAGE MODE)
              AND ts.team_id = :teamId AND ts.use_yn = 'Y'
            """;

        Long total =
                (Long)
                        entityManager
                                .createNativeQuery(countSql, Long.class)
                                .setParameter("teamId", teamId)
                                .setParameter("keyword", keyword)
                                .getSingleResult();
        return new SearchResult(ids, pageable, total);
    }
}
