package com.moyorak.api.team.domain;

import com.moyorak.infra.orm.AuditInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "team_invitation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamInvitation extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("팀 초대 고유 토큰")
    @Column(
            name = "invitation_token",
            nullable = false,
            unique = true,
            columnDefinition = "varchar(64)")
    private String invitationToken;

    @Comment("토큰 만료 일자")
    @Column(name = "expires_date", nullable = false)
    private LocalDateTime expiresDate;

    @Comment("팀 고유 ID")
    @Column(name = "team_id", nullable = false, columnDefinition = "bigint")
    private Long teamId;

    public static TeamInvitation create(
            String invitationToken, LocalDateTime expiresDate, Long teamId) {
        TeamInvitation teamInvitation = new TeamInvitation();

        teamInvitation.invitationToken = invitationToken;
        teamInvitation.expiresDate = expiresDate;
        teamInvitation.teamId = teamId;

        return teamInvitation;
    }

    public boolean isExpired(LocalDateTime now) {
        return expiresDate.isBefore(now);
    }

    public boolean isNotInTeam(Long teamId) {
        return !this.teamId.equals(teamId);
    }
}
