package com.moyorak.api.team.repository;

import com.moyorak.api.team.domain.TeamUser;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {

    @Query(
            """
        SELECT tu
        FROM TeamUser tu
        JOIN FETCH tu.team t
        JOIN FETCH t.company
        WHERE tu.userId = :userId
        AND tu.team.id = :teamId
        and tu.use = :use
""")
    Optional<TeamUser> findWithTeamAndCompany(
            @Param("teamId") Long teamId, @Param("userId") Long userId, @Param("use") boolean use);

    @Query(
            """
        SELECT tu
        FROM TeamUser tu
        JOIN Team t ON tu.team.id = t.id
        WHERE tu.userId = :userId AND tu.team.id = :teamId AND tu.use = :use
""")
    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value = "TeamUserRepository.findByUserIdAndTeamIdAndUse : 팀 멤버를 조회합니다."))
    Optional<TeamUser> findByUserIdAndTeamIdAndUse(
            @Param("userId") Long userId, @Param("teamId") Long teamId, @Param("use") boolean use);
}
