package com.moyorak.api.auth.domain;

import com.moyorak.infra.orm.AuditInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@Table(name = "member_daily_state")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDailyState extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @NotNull
    @Comment("회원 고유 ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @NotNull
    @Comment("혼밥 상태")
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, columnDefinition = "varchar(4)")
    private State state;

    @NotNull
    @Comment("혼밥 상태 기준 날짜")
    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    public static UserDailyState create(
            final Long userId, final State state, final LocalDate recordDate) {
        UserDailyState dailyState = new UserDailyState();

        dailyState.userId = userId;
        dailyState.state = state;
        dailyState.recordDate = recordDate;

        return dailyState;
    }

    public void changeState(final State state) {
        this.state = state;
    }
}
