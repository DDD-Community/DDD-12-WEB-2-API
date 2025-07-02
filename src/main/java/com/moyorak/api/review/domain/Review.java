package com.moyorak.api.review.domain;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.team.domain.TeamRestaurant;
import com.moyorak.infra.orm.AuditInformation;
import com.moyorak.infra.orm.BooleanYnConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Comment("평점")
    @Column(name = "score", nullable = false)
    private Integer score;

    @NotNull
    @Comment("음식 나오는 시간")
    @Column(name = "serving_time", nullable = false)
    private Integer servingTime;

    @NotNull
    @Comment("대기 시간")
    @Column(name = "waiting_time", nullable = false)
    private Integer waitingTime;

    @Comment("추가 텍스트")
    @Column(name = "extra_text", nullable = false, columnDefinition = "varchar(512)")
    private String extraText;

    @Comment("사용 여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private boolean use = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User user;

    @Comment("팀 맛집 고유 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_restaurant_id", nullable = false)
    private TeamRestaurant teamRestaurant;

    @OneToMany(mappedBy = "review_id")
    private List<ReviewPhoto> reviewPhotos = new ArrayList<>();
}
