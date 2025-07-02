package com.moyorak.api.team.domain;

import com.moyorak.infra.orm.AuditInformation;
import com.moyorak.infra.orm.BooleanYnConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "team_place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamPlace extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Comment("한줄 소개")
    @Column(name = "summary", columnDefinition = "varchar(32)")
    private String summary;

    @Comment("리뷰 평균 점수")
    @Column(name = "average_review_score", columnDefinition = "double")
    private double averageReviewScore;

    @Comment("팀에서 맛집까지 거리")
    @Column(name = "distance_from_team", columnDefinition = "double")
    private double distanceFromTeam;

    @Comment("사용 여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private boolean use = true;

    @Comment("team 고유 ID")
    @Column(name = "team_id", nullable = false, columnDefinition = "bigint")
    private Long teamId;

    @Comment("식당 고유 ID")
    @Column(name = "restaurant_id", nullable = false, columnDefinition = "bigint")
    private Long restaurantId;

    public static TeamPlace create(
            String summary, Long teamId, Long restaurantId, double distanceFromTeam) {
        TeamPlace teamPlace = new TeamPlace();

        teamPlace.summary = summary;
        teamPlace.teamId = teamId;
        teamPlace.restaurantId = restaurantId;
        teamPlace.distanceFromTeam = distanceFromTeam;
        teamPlace.averageReviewScore = 0.0;

        return teamPlace;
    }
}
