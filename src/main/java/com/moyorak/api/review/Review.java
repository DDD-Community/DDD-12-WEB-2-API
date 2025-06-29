package com.moyorak.api.review;

import com.moyorak.api.auth.domain.User;
import com.moyorak.api.restaurant.domain.Restaurant;
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
import jakarta.persistence.Table;
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

    @Comment("평점")
    @Column(name = "score", nullable = false)
    private Integer score;

    @Comment("음식 나오는 시간")
    @Column(name = "serving_time", nullable = false)
    private Integer serving_time;

    @Comment("대기 시간")
    @Column(name = "waiting_time", nullable = false)
    private Integer waiting_time;

    @Comment("추가 텍스트")
    @Column(name = "extra_text", nullable = false, columnDefinition = "varchar(512)")
    private String extra_text;

    @Comment("사용 여부")
    @Convert(converter = BooleanYnConverter.class)
    @Column(name = "use_yn", nullable = false, columnDefinition = "char(1)")
    private boolean use = true;

    @Comment("사용자 고유 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User user;

    @Comment("식당 고유 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
