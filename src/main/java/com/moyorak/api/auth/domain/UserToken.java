package com.moyorak.api.auth.domain;

import com.moyorak.infra.orm.AuditInformation;
import jakarta.persistence.Column;
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
@Table(name = "member_login_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserToken extends AuditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "bigint")
    private Long id;

    @Comment("회원 고유 ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Comment("액세스 토큰")
    @Column(name = "access_token", columnDefinition = "varchar(256)")
    private String accessToken;

    @Comment("리프레시 토큰")
    @Column(name = "refresh_token", columnDefinition = "varchar(256)")
    private String refreshToken;

    public static UserToken create(final Long userId, final String accessToken) {
        UserToken userToken = new UserToken();

        userToken.userId = userId;
        userToken.accessToken = accessToken;

        return userToken;
    }
}
