package com.moyorak.api.team.repository;

import com.moyorak.api.team.domain.TeamInvitation;
import com.moyorak.api.team.dto.TeamInvitationDetailResponse;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TeamInvitationRepository extends CrudRepository<TeamInvitation, Long> {

    Optional<TeamInvitation> findByInvitationToken(String invitationToken);

    @Query(
            """
SELECT new com.moyorak.api.team.dto.TeamInvitationDetailResponse(c.id, c.name, t.id, t.name)
FROM Team t
JOIN Company c ON t.company.id = c.id
WHERE t.id = :teamId AND t.use = :use
""")
    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value =
                            "TeamInvitationRepository.findInvitationDetailByTeamId : 팀 ID로 회사와 팀 정보를 조회합니다."))
    Optional<TeamInvitationDetailResponse> findInvitationDetailByTeamId(
            @Param("teamId") Long teamId, @Param("use") Boolean use);
}
